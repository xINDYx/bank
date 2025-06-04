package ru.yandex.exchange_service.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.yandex.exchange_service.model.Currency;
import ru.yandex.exchange_service.model.ExchangeRate;

@Repository
public interface ExchangeRepository extends ReactiveCrudRepository<ExchangeRate, Long> {
    Mono<ExchangeRate> findByCurrency(Currency currency);
}
