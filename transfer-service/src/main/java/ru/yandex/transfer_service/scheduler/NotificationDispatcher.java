package ru.yandex.transfer_service.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.bank.clients.accounts.AccountsClient;
import ru.yandex.transfer_service.dto.NotificationEvent;
import ru.yandex.transfer_service.mapper.NotificationMapper;
import ru.yandex.transfer_service.mapper.TransactionMapper;
import ru.yandex.transfer_service.model.Transaction;
import ru.yandex.transfer_service.model.TransactionStatus;
import ru.yandex.transfer_service.repository.TransactionRepository;
import ru.yandex.transfer_service.service.NotificationKafkaService;

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
                            .eventType("TRANSACTION_NOTIFICATION")
                            .recipient(userResponse.getEmail())
                            .subject(createSubject(transaction))
                            .message(createMessage(transaction, userResponse.getSurname() + " " + userResponse.getName()))
                            .accountId(transaction.getAccountId().toString())
                            .transactionId(transaction.getId().toString())
                            .transactionType("TRANSFER")
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
                return "Перевод выполнен успешно";
            case FAILED:
                return "Ошибка при выполнении перевода";
            case BLOCKED:
                return "Перевод заблокирован";
            case NOT_ENOUGH_MONEY:
                return "Недостаточно средств для перевода";
            default:
                return "Уведомление о переводе";
        }
    }

    private String createMessage(Transaction transaction, String userName) {
        StringBuilder message = new StringBuilder();
        message.append("Уважаемый ").append(userName).append(",\n\n");
        
        switch (transaction.getTransactionStatus()) {
            case COMPLETED:
                message.append("Ваш перевод на сумму ").append(transaction.getAmount())
                       .append(" RUB был успешно выполнен.\n");
                break;
            case FAILED:
                message.append("К сожалению, произошла ошибка при выполнении перевода на сумму ")
                       .append(transaction.getAmount()).append(" RUB.\n");
                break;
            case BLOCKED:
                message.append("Ваш перевод на сумму ").append(transaction.getAmount())
                       .append(" RUB был заблокирован системой безопасности.\n");
                break;
            case NOT_ENOUGH_MONEY:
                message.append("Недостаточно средств на счете для выполнения перевода на сумму ")
                       .append(transaction.getAmount()).append(" RUB.\n");
                break;
        }
        
        message.append("\nС уважением,\nБанковская система");
        return message.toString();
    }
}
