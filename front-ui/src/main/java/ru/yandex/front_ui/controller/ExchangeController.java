package ru.yandex.front_ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import ru.yandex.front_ui.service.ExchangeService;

@Controller
@RequestMapping("/exchange")
@RequiredArgsConstructor
public class ExchangeController {
    private final ExchangeService exchangeService;

    @GetMapping
    public Mono<String> readRates(Model model) {
        model.addAttribute("rates", exchangeService.readRates());
        return Mono.just("exchange");
    }
}
