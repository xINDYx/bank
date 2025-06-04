package ru.yandex.cash_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTransactionRequestDTO {
    @NotNull
    private Long accountId;
    @NotNull
    private Double amount;
}
