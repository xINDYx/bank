package ru.yandex.exchange_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.exchange_service.dto.ExchangeRateResponseDTO;
import ru.yandex.exchange_service.dto.UpdateExchangeRateRequestDTO;
import ru.yandex.exchange_service.mapper.ExchangeMapper;
import ru.yandex.exchange_service.model.Currency;
import ru.yandex.exchange_service.model.ExchangeRate;
import ru.yandex.exchange_service.repository.ExchangeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeMapper exchangeMapper;
    private final ExchangeRepository exchangeRepository;

    public Flux<ExchangeRateResponseDTO> readExchangeRates() {
        return exchangeRepository.findAll()
                .map(exchangeMapper::map);
    }

    public Mono<ExchangeRateResponseDTO> readExchangeRateByCurrency(Currency currency) {
        return exchangeRepository.findByCurrency(currency)
                .map(exchangeMapper::map);
    }

    public Flux<ExchangeRateResponseDTO> save(List<UpdateExchangeRateRequestDTO> exchangeRates) {
        return Flux.fromIterable(exchangeRates)
                .map(exchangeMapper::map)
                .collectList()
                .flatMap(this::setIdsForExistingRates)
                .flatMapMany(exchangeRepository::saveAll)
                .map(exchangeMapper::map);
    }

    private Mono<List<ExchangeRate>> setIdsForExistingRates(List<ExchangeRate> exchangeRates) {
        return exchangeRepository.findAll()
                .collectMap(ExchangeRate::getCurrency, rate -> rate)
                .map(currencyToIdMap -> {
                    exchangeRates.forEach(rate -> {
                        if (currencyToIdMap.containsKey(rate.getCurrency())) {
                            var existing = currencyToIdMap.get(rate.getCurrency());
                            rate.setId(existing.getId());
                            rate.setCreatedAt(existing.getCreatedAt());
                            rate.setModifiedAt(existing.getModifiedAt());
                        }
                    });
                    return exchangeRates;
                });


    }

    public Mono<Void> updateExchangeRateFromEvent(ExchangeRateResponseDTO event) {
        // Обновляем или сохраняем курс валюты по событию
        return exchangeRepository.findByCurrency(event.getCurrency())
                .defaultIfEmpty(new ExchangeRate())
                .flatMap(existing -> {
                    existing.setCurrency(event.getCurrency());
                    existing.setRate(event.getRate());
                    existing.setCreatedAt(event.getCreatedAt());
                    existing.setModifiedAt(event.getModifiedAt());
                    return exchangeRepository.save(existing);
                })
                .then();
    }
}
