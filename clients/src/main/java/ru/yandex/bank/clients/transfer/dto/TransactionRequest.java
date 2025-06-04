package ru.yandex.bank.clients.transfer.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionRequest {
    private Long accountId;
    private Long receiverAccountId;
    private Double amount;

}
