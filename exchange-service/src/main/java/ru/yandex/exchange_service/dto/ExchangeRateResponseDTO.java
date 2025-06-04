package ru.yandex.exchange_service.dto;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.exchange_service.model.Currency;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExchangeRateResponseDTO {
    private Currency currency;
    private Double rate;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
