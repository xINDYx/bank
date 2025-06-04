package ru.yandex.exchange_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.exchange_service.dto.ExchangeRateResponseDTO;
import ru.yandex.exchange_service.dto.UpdateExchangeRateRequestDTO;
import ru.yandex.exchange_service.model.Currency;
import ru.yandex.exchange_service.service.ExchangeService;

import java.util.List;

@RestController
@RequestMapping("/exchange-rates")
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeService exchangeService;

    @GetMapping
    public Flux<ExchangeRateResponseDTO> read() {
        return exchangeService.readExchangeRates();
    }

    @GetMapping("/{currency}")
    public Mono<ExchangeRateResponseDTO> readByCurrency(@PathVariable @Valid String currency) {
        return exchangeService.readExchangeRateByCurrency(Currency.valueOf(currency));
    }

    @PostMapping
    public Flux<ExchangeRateResponseDTO> save(@RequestBody @Valid List<UpdateExchangeRateRequestDTO> request) {
        return exchangeService.save(request);
    }
}
