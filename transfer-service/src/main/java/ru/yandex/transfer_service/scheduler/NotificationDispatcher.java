package ru.yandex.transfer_service.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.bank.clients.accounts.AccountsClient;
import ru.yandex.bank.clients.notification.NotificationClient;
import ru.yandex.transfer_service.mapper.NotificationMapper;
import ru.yandex.transfer_service.mapper.TransactionMapper;
import ru.yandex.transfer_service.model.Transaction;
import ru.yandex.transfer_service.model.TransactionStatus;
import ru.yandex.transfer_service.repository.TransactionRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationDispatcher {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountsClient accountsClient;
    private final NotificationClient notificationClient;
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
                .map(userResponse -> notificationMapper.map(transaction, userResponse.getEmail(),
                        userResponse.getSurname() + " " + userResponse.getName()))
                .flatMap(notificationClient::sendEmailNotification)
                .flatMap(__ -> {
                    transaction.setNotificationSent(true);
                    return transactionRepository.save(transactionMapper.mapToDb(transaction));
                })
                .map(transactionMapper::map);
    }
}
