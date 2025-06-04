package ru.yandex.accounts_service.exception;

public class DeletionAccountWithFundsException extends RuntimeException {
    public DeletionAccountWithFundsException() {
        super("Deletion account with some funds was canceled");
    }
}
