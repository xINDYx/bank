package ru.yandex.cash_service.dto;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.cash_service.model.TransactionStatus;
import ru.yandex.cash_service.model.TransactionType;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionResponseDTO {
    private Long id;
    private Long accountId;
    private Double amount;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private LocalDateTime createdAt;
}
