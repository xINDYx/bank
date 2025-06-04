package ru.yandex.front_ui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.bank.clients.accounts.AccountsClient;
import ru.yandex.bank.clients.accounts.dto.user.FindByLoginRequest;
import ru.yandex.front_ui.dto.user.*;
import ru.yandex.front_ui.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService {
    private final AccountsClient accountsClient;
    private final UserMapper userMapper;

    @Autowired
    public UserService(@Value("${clients.accounts.uri}") String accountClientUrl,
                       WebClient webClient,
                       UserMapper userMapper) {
        this.accountsClient = new AccountsClient(accountClientUrl, webClient);
        this.userMapper = userMapper;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return accountsClient
                .findByLogin(FindByLoginRequest.builder()
                        .login(username)
                        .build())
                .map(userMapper::mapToUser);
    }

    public Mono<UserResponseDTO> register(RegisterUserRequestDTO request) {
        return accountsClient
                .registerUser(userMapper.map(request))
                .map(userMapper::map);
    }

    public Mono<UserResponseDTO> update(Long userId, UpdateUserRequestDTO request) {
        return accountsClient.updateUser(userId, userMapper.map(request))
                .map(userMapper::map);
    }

    public Mono<UserResponseDTO> changePassword(Long userId, ChangePasswordRequestDTO request) {
        return accountsClient.changePassword(userId, userMapper.map(request))
                .map(userMapper::map);
    }

    public Flux<UserAccountResponseDTO> readUsersWithAccounts() {
        return accountsClient.readUsersWithAccounts()
                .map(userMapper::map);
    }
}
