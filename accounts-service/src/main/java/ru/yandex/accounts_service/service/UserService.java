package ru.yandex.accounts_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.accounts_service.dto.user.FindByLoginRequestDto;
import ru.yandex.accounts_service.dto.user.PasswordChangeRequestDto;
import ru.yandex.accounts_service.dto.user.UserAccountsResponseDto;
import ru.yandex.accounts_service.dto.user.UserRegisterRequestDto;
import ru.yandex.accounts_service.dto.user.UserResponseDto;
import ru.yandex.accounts_service.dto.user.UserUpdateRequestDto;
import ru.yandex.accounts_service.exception.UserNotFoundException;
import ru.yandex.accounts_service.mapper.AccountMapper;
import ru.yandex.accounts_service.mapper.UserMapper;
import ru.yandex.accounts_service.model.Account;
import ru.yandex.accounts_service.repository.AccountRepository;
import ru.yandex.accounts_service.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public Mono<UserResponseDto> findByLogin(FindByLoginRequestDto request) {
        return userRepository.findByLogin(request.getLogin())
                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                .map(userMapper::map);
    }

    public Mono<UserResponseDto> findByAccountId(Long accountId) {
        return userRepository.findByAccountId(accountId)
                .switchIfEmpty(Mono.error(new UserNotFoundException()))
                .map(userMapper::map);
    }

    public Mono<UserResponseDto> registerUser(UserRegisterRequestDto request) {
        return userRepository.save(userMapper.map(request))
                .map(userMapper::map);
    }

    public Mono<UserResponseDto> update(Long userId, UserUpdateRequestDto request) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException(userId)))
                .map(u -> userMapper.update(request, u))
                .flatMap(userRepository::save)
                .map(userMapper::map);
    }

    public Mono<UserResponseDto> changePassword(Long userId, PasswordChangeRequestDto request) {
        return userRepository.findById(userId)
                .switchIfEmpty(Mono.error(new UserNotFoundException(userId)))
                .doOnNext(u -> u.setPassword(passwordEncoder.encode(request.getPassword())))
                .flatMap(userRepository::save)
                .map(userMapper::map);
    }

    public Flux<UserAccountsResponseDto> findAllUsersWithAccounts() {
        return userRepository.findAll()
                .map(userMapper::mapUserAccounts)
                .collectList()
                .zipWith(accountRepository.findAll().collectList())
                .flatMapMany(tuple -> {
                    var users = tuple.getT1();
                    var accounts = tuple.getT2();
                    return Flux.fromIterable(collectUserAccounts(users, accounts));
                })
                .filter(u -> u.getAccounts() != null);
    }

    private Collection<UserAccountsResponseDto> collectUserAccounts(List<UserAccountsResponseDto> users,
                                                                    List<Account> accounts) {
        var usersMap = new HashMap<Long, UserAccountsResponseDto>();
        users.forEach(u -> usersMap.put(u.getId(), u));
        accounts.forEach(a -> {
            var user = usersMap.get(a.getUserId());
            if (user.getAccounts() == null) {
                user.setAccounts(new ArrayList<>());
            }
            user.getAccounts().add(accountMapper.map(a));
        });
        return usersMap.values();
    }
}
