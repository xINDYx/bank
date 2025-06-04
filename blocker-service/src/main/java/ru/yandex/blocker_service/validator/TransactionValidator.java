package ru.yandex.blocker_service.validator;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.yandex.blocker_service.exception.UnknownTransactionTypeException;
import ru.yandex.blocker_service.model.DepositTransaction;
import ru.yandex.blocker_service.model.SelfTransferTransaction;
import ru.yandex.blocker_service.model.Transaction;
import ru.yandex.blocker_service.model.TransferToOtherUserTransaction;
import ru.yandex.blocker_service.model.WithdrawTransaction;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class TransactionValidator {
    public static Mono<Boolean> validate(Transaction transaction) {
        return switch (transaction) {
            case DepositTransaction depositTransaction -> Mono.just(TransactionValidator
                    .validate(depositTransaction));

            case WithdrawTransaction withdrawTransaction -> Mono.just(TransactionValidator
                    .validate(withdrawTransaction));

            case SelfTransferTransaction selfTransferTransaction ->
                    Mono.just(TransactionValidator.validate(selfTransferTransaction));

            case TransferToOtherUserTransaction transferToAnotherUserTransaction ->
                    Mono.just(TransactionValidator.validate(transferToAnotherUserTransaction));

            default -> throw new UnknownTransactionTypeException(transaction.getClass().getSimpleName());
        };
    }

    private static boolean validate(WithdrawTransaction transaction) {
        return validationWithRandomDecisionAtRiskyTime(transaction.getCreatedAt(), 0.7);
    }

    private static boolean validate(DepositTransaction transaction) {
        return validationWithRandomDecisionAtRiskyTime(transaction.getCreatedAt(), 0.9);
    }

    private static boolean validate(SelfTransferTransaction transaction) {
        return validationWithRandomDecisionAtRiskyTime(transaction.getCreatedAt(), 0.8);
    }

    private static boolean validate(TransferToOtherUserTransaction transaction) {
        return validationWithRandomDecisionAtRiskyTime(transaction.getCreatedAt(), 0.6);
    }

    private static boolean validationWithRandomDecisionAtRiskyTime(LocalDateTime createdAt, double factor) {
        LocalTime transactionTime = createdAt.toLocalTime();
        if (transactionTime.isAfter(LocalTime.of(9, 0))
                && transactionTime.isBefore(LocalTime.of(18, 0))) {
            return true;
        } else if (transactionTime.isAfter(LocalTime.of(18, 0))
                && transactionTime.isBefore(LocalTime.of(21, 0))) {
            return Math.random() < factor;
        } else {
            return false;
        }
    }
}
