package ru.yandex.bank.clients.cash.dto;

public enum TransactionStatus {
    CREATED,
    APPROVED,
    BLOCKED,
    COMPLETED,
    NOT_ENOUGH_MONEY,
    FAILED
}
