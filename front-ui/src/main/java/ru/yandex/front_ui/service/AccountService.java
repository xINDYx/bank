package ru.yandex.front_ui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.bank.clients.accounts.AccountsClient;
import ru.yandex.front_ui.dto.account.AccountResponseDTO;
import ru.yandex.front_ui.dto.account.CreateAccountRequestDTO;
import ru.yandex.front_ui.mapper.AccountMapper;
import ru.yandex.front_ui.utils.SecurityUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountsClient accountsClient;
    private final AccountMapper accountMapper;

    private final List<String> defaultCurrencyList = List.of("USD", "RUB", "CNY");


    public Flux<AccountResponseDTO> readAccountsOfUser() {
        return SecurityUtils.getUserId()
                .flatMapMany(accountsClient::readAccountsOfUser)
                .map(accountMapper::map);
    }

    public Flux<AccountResponseDTO> readAccountsOfUserById(Long userId) {
        return accountsClient.readAccountsOfUser(userId)
                .map(accountMapper::map);
    }

    public Mono<Void> deleteAccount(Long userId) {
        return accountsClient.deleteAccount(userId);
    }

    @PreAuthorize("#request.userId == authentication.principal.id")
    public Mono<AccountResponseDTO> createAccount(CreateAccountRequestDTO request) {
        return accountsClient.createAccount(accountMapper.map(request))
                .map(accountMapper::map);
    }

    public Mono<List<String>> notExistingAccountCurrencyListOfUser() {
        return readAccountsOfUser()
                .map(AccountResponseDTO::getCurrency)
                .collectList()
                .map(existing -> defaultCurrencyList.stream()
                        .filter(item -> !existing.contains(item)).toList());
    }
}
