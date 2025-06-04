package ru.yandex.exchange_generator_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.bank.clients.exchange.ExchangeClient;
import ru.yandex.bank.clients.exchange.dto.ExchangeRateResponse;
import ru.yandex.exchange_generator_service.mapper.ExchangeMapper;
import ru.yandex.exchange_generator_service.model.Currency;
import ru.yandex.exchange_generator_service.model.ExchangeRate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeMapper exchangeMapper;
    private final ExchangeClient exchangeClient;

    @Scheduled(fixedDelay = 1000)
    public Flux<ExchangeRateResponse> updateRates() {
        return generateRates()
                .flatMapMany(Flux::fromIterable)
                .map(exchangeMapper::map)
                .collectList()
                .flatMapMany(exchangeClient::update);
    }

    private Mono<List<ExchangeRate>> generateRates() {
        return Flux.just(Currency.values())
                .map(currency -> ExchangeRate.builder()
                        .currency(currency)
                        .rate(generateRateForCurrency(currency))
                        .build())
                .collectList();
    }

    private double generateRateForCurrency(Currency currency) {
        return switch (currency) {
            case RUB -> 1.0;
            case USD -> getRandomNumber(0.01, 0.015);
            case CNY -> getRandomNumber(0.075, 0.085);
        };
    }

    private double getRandomNumber(double min, double max) {
        return ((Math.random() * (max - min)) + min);
    }
}
