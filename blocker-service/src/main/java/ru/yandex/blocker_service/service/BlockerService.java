package ru.yandex.blocker_service.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.blocker_service.dto.DecisionResponseDto;
import ru.yandex.blocker_service.model.Transaction;
import ru.yandex.blocker_service.validator.TransactionValidator;

@Service
public class BlockerService {
    public Mono<DecisionResponseDto> verify(Transaction transaction) {
        return TransactionValidator.validate(transaction)
                .map(decision -> DecisionResponseDto.builder()
                        .isBlocked(!decision)
                        .build());
    }
}
