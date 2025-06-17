package ru.yandex.exchange_generator_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.exchange_generator_service.model.Currency;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateEvent {
    private Currency currency;
    private Double rate;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
} 