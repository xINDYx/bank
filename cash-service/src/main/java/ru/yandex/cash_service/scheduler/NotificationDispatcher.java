package ru.yandex.cash_service.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.cash_service.dto.NotificationEvent;
import ru.yandex.cash_service.mapper.NotificationMapper;
import ru.yandex.cash_service.mapper.TransactionMapper;
import ru.yandex.cash_service.model.Transaction;
import ru.yandex.cash_service.model.TransactionStatus;
import ru.yandex.cash_service.repository.TransactionRepository;
import ru.yandex.cash_service.service.NotificationKafkaService;
import ru.yandex.bank.clients.accounts.AccountsClient;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationDispatcher {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountsClient accountsClient;
    private final NotificationKafkaService notificationKafkaService;
    private final NotificationMapper notificationMapper;

    @Scheduled(fixedDelay = 3000)
    public Flux<Transaction> sendNotifications() {
        return transactionRepository.findByTransactionStatusInAndNotificationSent(
                        List.of(TransactionStatus.BLOCKED, TransactionStatus.COMPLETED,
                                TransactionStatus.FAILED, TransactionStatus.NOT_ENOUGH_MONEY), false)
                .map(transactionMapper::map)
                .flatMap(this::sendEmail);
    }

    private Mono<Transaction> sendEmail(Transaction transaction) {
        return accountsClient.findUserByAccountId(transaction.getAccountId())
                .flatMap(userResponse -> {
                    // Создаем событие уведомления
                    NotificationEvent event = NotificationEvent.builder()
                            .eventType("CASH_TRANSACTION_NOTIFICATION")
                            .recipient(userResponse.getEmail())
                            .subject(createSubject(transaction))
                            .message(createMessage(transaction))
                            .accountId(transaction.getAccountId().toString())
                            .transactionId(transaction.getId().toString())
                            .transactionType("CASH")
                            .amount(BigDecimal.valueOf(transaction.getAmount()))
                            .currency("RUB")
                            .status(transaction.getTransactionStatus().name())
                            .build();

                    // Отправляем событие в Kafka
                    return notificationKafkaService.sendNotificationEvent(event)
                            .then(Mono.defer(() -> {
                                transaction.setNotificationSent(true);
                                return transactionRepository.save(transactionMapper.mapToDb(transaction));
                            }))
                            .map(transactionMapper::map);
                });
    }

    private String createSubject(Transaction transaction) {
        switch (transaction.getTransactionStatus()) {
            case COMPLETED:
                return "Операция с наличными выполнена успешно";
            case FAILED:
                return "Ошибка при выполнении операции с наличными";
            case BLOCKED:
                return "Операция с наличными заблокирована";
            case NOT_ENOUGH_MONEY:
                return "Недостаточно средств для операции с наличными";
            default:
                return "Уведомление об операции с наличными";
        }
    }

    private String createMessage(Transaction transaction) {
        StringBuilder message = new StringBuilder();
        message.append("Уважаемый клиент,\n\n");
        
        switch (transaction.getTransactionStatus()) {
            case COMPLETED:
                message.append("Ваша операция с наличными на сумму ").append(transaction.getAmount())
                       .append(" RUB была успешно выполнена.\n");
                break;
            case FAILED:
                message.append("К сожалению, произошла ошибка при выполнении операции с наличными на сумму ")
                       .append(transaction.getAmount()).append(" RUB.\n");
                break;
            case BLOCKED:
                message.append("Ваша операция с наличными на сумму ").append(transaction.getAmount())
                       .append(" RUB была заблокирована системой безопасности.\n");
                break;
            case NOT_ENOUGH_MONEY:
                message.append("Недостаточно средств на счете для выполнения операции с наличными на сумму ")
                       .append(transaction.getAmount()).append(" RUB.\n");
                break;
        }
        
        message.append("\nС уважением,\nБанковская система");
        return message.toString();
    }
}
