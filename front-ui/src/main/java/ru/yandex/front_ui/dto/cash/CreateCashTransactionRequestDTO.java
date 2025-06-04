package ru.yandex.front_ui.dto.cash;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCashTransactionRequestDTO {
    @NotNull
    private Long accountId;
    @NotNull
    private Double amount;
}
