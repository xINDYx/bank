package ru.yandex.accounts_service.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import ru.yandex.accounts_service.model.Account;

public interface AccountRepository extends R2dbcRepository<Account, Long> {
    Flux<Account> findByUserId(Long userId);
}
