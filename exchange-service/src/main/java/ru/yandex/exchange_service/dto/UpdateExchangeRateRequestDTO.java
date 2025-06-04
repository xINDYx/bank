package ru.yandex.exchange_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.exchange_service.model.Currency;

@Getter
@Setter
public class UpdateExchangeRateRequestDTO {
    @NotNull
    private Currency currency;
    @NotNull
    @Positive
    private Double rate;
}
