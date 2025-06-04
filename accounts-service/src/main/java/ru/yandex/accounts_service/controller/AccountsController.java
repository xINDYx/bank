package ru.yandex.accounts_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.accounts_service.dto.account.AccountCreateRequestDto;
import ru.yandex.accounts_service.dto.account.AccountResponseDto;
import ru.yandex.accounts_service.dto.account.DepositMoneyDto;
import ru.yandex.accounts_service.dto.account.TransferMoneyRequestDto;
import ru.yandex.accounts_service.dto.account.TransferMoneyResponseDto;
import ru.yandex.accounts_service.dto.account.WithdrawMoneyDto;
import ru.yandex.accounts_service.service.AccountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountsController {

    private final AccountService accountService;

    @GetMapping("/{accountId}")
    public Mono<AccountResponseDto> getAccountById(@PathVariable Long accountId) {
        return accountService.getAccountById(accountId);
    }

    @GetMapping("/user/{userId}")
    public Flux<AccountResponseDto> getUserAccounts(@PathVariable Long userId) {
        return accountService.getUserAccounts(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AccountResponseDto> createAccount(@RequestBody AccountCreateRequestDto request) {
        return accountService.createAccount(request);
    }

    @DeleteMapping("/{accountId}")
    public Mono<Void> deleteAccount(@PathVariable Long accountId) {
        return accountService.deleteAccount(accountId);
    }

    @PutMapping("/{accountId}/deposit")
    public Mono<AccountResponseDto> depositToAccount(@PathVariable Long accountId, @RequestBody DepositMoneyDto request) {
        return accountService.depositMoney(accountId, request.getAmount());
    }

    @PutMapping("/{accountId}/withdraw")
    public Mono<AccountResponseDto> withdrawFromAccount(@PathVariable Long accountId, @RequestBody WithdrawMoneyDto request) {
        return accountService.withdrawMoney(accountId, request.getAmount());
    }

    @PutMapping("/transfer")
    public Mono<TransferMoneyResponseDto> transferMoney(@RequestBody TransferMoneyRequestDto request) {
        return accountService.transferMoney(request);
    }
}
