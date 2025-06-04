package ru.yandex.bank.clients.accounts.exception;

public class SignupException extends RuntimeException {
    public SignupException(String message) {
        super(message);
    }
}
