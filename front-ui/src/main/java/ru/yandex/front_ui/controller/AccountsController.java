package ru.yandex.front_ui.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;
import ru.yandex.front_ui.dto.account.CreateAccountRequestDTO;
import ru.yandex.front_ui.service.AccountService;
import ru.yandex.front_ui.utils.SecurityUtils;

@Controller
@RequiredArgsConstructor
public class AccountsController {
    private final AccountService accountService;

    @GetMapping
    public Mono<String> readAccountsOfUser(Model model) {
        model.addAttribute("accounts", accountService.readAccountsOfUser());
        model.addAttribute("userId", SecurityUtils.getUserId());
        model.addAttribute("currencyList", accountService.notExistingAccountCurrencyListOfUser());
        return Mono.just("accounts");
    }

    @PostMapping("/accounts")
    public Mono<String> createAccount(@ModelAttribute @Valid CreateAccountRequestDTO request) {
        return accountService.createAccount(request)
                .then(Mono.just("redirect:/"));
    }

    @PostMapping("/accounts/{accountId}/delete")
    public Mono<String> deleteAccount(@PathVariable Long accountId) {
        return accountService.deleteAccount(accountId)
                .then(Mono.just("redirect:/"));
    }
}
