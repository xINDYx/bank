package ru.yandex.bank.clients.accounts.exception;

public class MoneyException extends RuntimeException {
    public MoneyException(String message) {
        super(message);
    }
}
