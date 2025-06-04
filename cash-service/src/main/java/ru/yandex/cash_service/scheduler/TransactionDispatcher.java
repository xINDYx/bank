package ru.yandex.cash_service.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.cash_service.mapper.TransactionMapper;
import ru.yandex.cash_service.model.DepositTransaction;
import ru.yandex.cash_service.model.Transaction;
import ru.yandex.cash_service.model.TransactionStatus;
import ru.yandex.cash_service.model.WithdrawalTransaction;
import ru.yandex.cash_service.repository.TransactionRepository;
import ru.yandex.bank.clients.accounts.AccountsClient;
import ru.yandex.bank.clients.accounts.dto.accounts.AccountResponse;
import ru.yandex.bank.clients.accounts.dto.accounts.PutMoneyToAccount;
import ru.yandex.bank.clients.accounts.dto.accounts.TakeMoneyFromAccount;
import ru.yandex.bank.clients.accounts.exception.MoneyException;

@Component
@RequiredArgsConstructor
public class TransactionDispatcher {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountsClient accountsClient;

    @Scheduled(fixedDelay = 3000)
    public Flux<Transaction> processApprovedTransactions() {
        return transactionRepository
                .findByTransactionStatus(TransactionStatus.APPROVED)
                .map(transactionMapper::map)
                .flatMap(transaction -> processTransaction(transaction)
                        .onErrorResume(e -> {
                            if (e instanceof MoneyException) {
                                transaction.setTransactionStatus(TransactionStatus.NOT_ENOUGH_MONEY);
                            } else {
                                transaction.setTransactionStatus(TransactionStatus.FAILED);
                            }

                            return transactionRepository.save(transactionMapper.mapToDb(transaction))
                                    .flatMap(__ -> Mono.error(e));
                        })
                        .flatMap(account -> {
                            transaction.setTransactionStatus(TransactionStatus.COMPLETED);
                            return transactionRepository.save(transactionMapper.mapToDb(transaction));
                        })
                )
                .map(transactionMapper::map);
    }

    private Mono<AccountResponse> processTransaction(Transaction transaction) {
        return switch (transaction) {
            case DepositTransaction depositTransaction -> accountsClient.putMoneyToAccount(transaction.getAccountId(),
                    PutMoneyToAccount.builder()
                            .amount(transaction.getAmount())
                            .build());
            case WithdrawalTransaction withdrawalTransaction ->
                    accountsClient.takeMoneyFromAccount(transaction.getAccountId(),
                            TakeMoneyFromAccount.builder()
                                    .amount(transaction.getAmount())
                                    .build());
            default -> throw new IllegalStateException("Unexpected value: " + transaction);
        };
    }
}
