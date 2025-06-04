package ru.yandex.transfer_service.exception;

public class UnknownTransactionTypeException extends RuntimeException {
    public UnknownTransactionTypeException(String message) {
        super(message);
    }
}
