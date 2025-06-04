package ru.yandex.transfer_service.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.bank.clients.blocker.BlockerClient;
import ru.yandex.bank.clients.blocker.dto.DecisionResponse;
import ru.yandex.transfer_service.exception.UnknownTransactionTypeException;
import ru.yandex.transfer_service.mapper.TransactionMapper;
import ru.yandex.transfer_service.model.SelfTransferTransaction;
import ru.yandex.transfer_service.model.Transaction;
import ru.yandex.transfer_service.model.TransactionStatus;
import ru.yandex.transfer_service.model.TransferToOtherUserTransaction;
import ru.yandex.transfer_service.repository.TransactionRepository;

@Component
@RequiredArgsConstructor
public class TransactionValidator {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final BlockerClient blockerClient;

    @Scheduled(fixedDelay = 3000)
    public Flux<Transaction> validateCreatedTransactions() {
        return transactionRepository
                .findByTransactionStatus(TransactionStatus.CREATED)
                .map(transactionMapper::map)
                .flatMap(transaction -> validate(transaction)
                        .flatMap(decisionResponse -> {
                            transaction.setTransactionStatus(decisionResponse.isBlocked() ?
                                    TransactionStatus.BLOCKED :
                                    TransactionStatus.APPROVED);
                            return transactionRepository.save(transactionMapper.mapToDb(transaction));
                        }))
                .map(transactionMapper::map)
                .onErrorResume(e -> {
                    //TODO - log error
                    return Mono.empty();
                });
    }

    private Mono<DecisionResponse> validate(Transaction transaction) {
        return switch (transaction) {
            case SelfTransferTransaction selfTransferTransaction ->
                    blockerClient.validate(transactionMapper.map(selfTransferTransaction));
            case TransferToOtherUserTransaction transferToOtherUserTransaction ->
                    blockerClient.validate(transactionMapper.map(transferToOtherUserTransaction));
            default -> throw new UnknownTransactionTypeException("Unexpected value: "
                    + transaction.getClass().getSimpleName());
        };
    }
}
