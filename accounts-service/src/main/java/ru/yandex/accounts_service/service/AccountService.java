package ru.yandex.accounts_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.accounts_service.dto.account.AccountCreateRequestDto;
import ru.yandex.accounts_service.dto.account.AccountResponseDto;
import ru.yandex.accounts_service.dto.account.TransferMoneyRequestDto;
import ru.yandex.accounts_service.dto.account.TransferMoneyResponseDto;
import ru.yandex.accounts_service.exception.DeletionAccountWithFundsException;
import ru.yandex.accounts_service.exception.NotEnoughMoneyException;
import ru.yandex.accounts_service.exception.NotFoundException;
import ru.yandex.accounts_service.mapper.AccountMapper;
import ru.yandex.accounts_service.model.Account;
import ru.yandex.accounts_service.model.Currency;
import ru.yandex.accounts_service.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public Mono<AccountResponseDto> createAccount(AccountCreateRequestDto request) {
        var currency = Currency.valueOf(request.getCurrency());
        var account = new Account(
                request.getUserId(),
                currency
        );
        return accountRepository.save(account)
                .onErrorResume(Mono::error)
                .map(accountMapper::map);
    }

    public Mono<Void> deleteAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new NotFoundException("There is no account with id = " + accountId)))
                .doOnNext(a -> {
                    if (a.getAmount() != 0) {
                        throw new DeletionAccountWithFundsException();
                    }
                })
                .flatMap(a -> accountRepository.deleteById(accountId));
    }

    public Mono<AccountResponseDto> getAccountById(Long accountId) {
        return accountRepository.findById(accountId).map(accountMapper::map);
    }

    public Flux<AccountResponseDto> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId).map(accountMapper::map);
    }

    @Transactional
    public Mono<AccountResponseDto> depositMoney(Long accountId, Double amount) {
        return accountRepository.findById(accountId)
                .doOnNext(a -> a.setAmount(a.getAmount() + amount))
                .flatMap(accountRepository::save)
                .map(accountMapper::map);
    }

    @Transactional
    public Mono<AccountResponseDto> withdrawMoney(Long accountId, Double amount) {
        return accountRepository.findById(accountId)
                .doOnNext(a -> {
                    if (a.getAmount() < amount) {
                        throw new NotEnoughMoneyException();
                    }
                    a.setAmount(a.getAmount() - amount);
                })
                .flatMap(accountRepository::save)
                .map(accountMapper::map);
    }

    @Transactional
    public Mono<TransferMoneyResponseDto> transferMoney(TransferMoneyRequestDto request) {
        return Mono.zip(
                        accountRepository.findById(request.getFromAccountId()),
                        accountRepository.findById(request.getToAccountId())
                )
                .flatMap(tuple -> {
                    Account fromAccount = tuple.getT1();
                    Account toAccount = tuple.getT2();
                    Double fromAmount = request.getFromAmount();
                    Double toAmount = request.getToAmount();

                    if (fromAccount.getAmount() < fromAmount) {
                        return Mono.error(new NotEnoughMoneyException());
                    }

                    fromAccount.setAmount(fromAccount.getAmount() - fromAmount);
                    toAccount.setAmount(toAccount.getAmount() + toAmount);

                    return accountRepository.save(fromAccount)
                            .then(accountRepository.save(toAccount));
                })
                .thenReturn(TransferMoneyResponseDto.builder().completed(true).build());
    }
}
