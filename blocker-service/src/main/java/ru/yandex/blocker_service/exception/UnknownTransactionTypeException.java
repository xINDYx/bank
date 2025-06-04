package ru.yandex.blocker_service.exception;

public class UnknownTransactionTypeException extends RuntimeException {
    public UnknownTransactionTypeException(String message) {
        super("Unknown transaction type: " + message);
    }
}
