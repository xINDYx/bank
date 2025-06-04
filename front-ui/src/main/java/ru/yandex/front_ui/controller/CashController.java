package ru.yandex.front_ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import ru.yandex.front_ui.dto.cash.CreateCashTransactionRequestDTO;
import ru.yandex.front_ui.service.AccountService;
import ru.yandex.front_ui.service.CashService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cash")
public class CashController {
    private final CashService cashService;
    private final AccountService accountService;

    @GetMapping
    private Mono<String> cashPage(Model model) {
        model.addAttribute("accounts", accountService.readAccountsOfUser());
        return Mono.just("cash");
    }

    @PostMapping("/deposit")
    private Mono<String> deposit(@ModelAttribute CreateCashTransactionRequestDTO request, Model model) {
        return cashService.deposit(request)
                .flatMap(__ -> Mono.just("redirect:/cash"));
    }

    @PostMapping("/withdrawal")
    private Mono<String> withdrawal(@ModelAttribute CreateCashTransactionRequestDTO request, Model model) {
        return cashService.withdrawal(request)
                .flatMap(__ -> Mono.just("redirect:/cash"));
    }
}
