package ru.yandex.accounts_service.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.yandex.accounts_service.model.User;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {
    Mono<User> findByLogin(String login);

    @Query("""
        SELECT u.id, u.login, u.first_name, u.last_name, u.email
        FROM users u
            LEFT JOIN accounts a ON u.id = a.user_id
        WHERE a.id = :accountId
        """)
    Mono<User> findByAccountId(Long accountId);
}
