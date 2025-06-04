package ru.yandex.front_ui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.yandex.bank.clients.exchange.ExchangeClient;
import ru.yandex.front_ui.dto.exchange.ExchangeRateResponseDTO;
import ru.yandex.front_ui.mapper.ExchangeMapper;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeClient exchangeClient;
    private final ExchangeMapper exchangeMapper;

    public Flux<ExchangeRateResponseDTO> readRates() {
        return exchangeClient.readAll()
                .map(exchangeMapper::map);
    }
}
