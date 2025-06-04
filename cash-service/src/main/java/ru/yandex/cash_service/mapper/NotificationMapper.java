package ru.yandex.cash_service.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.cash_service.model.DepositTransaction;
import ru.yandex.cash_service.model.Transaction;
import ru.yandex.cash_service.model.WithdrawalTransaction;
import ru.yandex.bank.clients.notification.dto.CreateEmailNotificationRequest;

@Component
public class NotificationMapper {
    public CreateEmailNotificationRequest map(Transaction transaction, String email) {
        String operationType = switch (transaction) {
            case DepositTransaction depositTransaction -> "внесения";
            case WithdrawalTransaction withdrawalTransaction -> "снятия";
            default -> throw new RuntimeException("Неизвестный тип транзакци");
        };

        CreateEmailNotificationRequest request = CreateEmailNotificationRequest
                .builder()
                .recipient(email)
                .subject(String.format("Результат выполнения операции %s денежных средств по вашему счету %d",
                        operationType, transaction.getAccountId()))
                .build();

        switch (transaction.getTransactionStatus()) {
            case BLOCKED -> request.setMessage("Операция заблокирована");
            case FAILED -> request.setMessage("Ошибка совершения операции! Пожалуйста попробуйте позже...");
            case COMPLETED -> request.setMessage("Операция выполнена успешно");
            case NOT_ENOUGH_MONEY -> request.setMessage("Недостаточно денег для совершения операции");
        }

        return request;
    }
}
