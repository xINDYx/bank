package ru.yandex.exchange_generator_service.dto;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.exchange_generator_service.model.Currency;

@Getter
@Setter
public class ExchangeRateResponseDTO {
    private Currency currency;
    private Double rate;
}
