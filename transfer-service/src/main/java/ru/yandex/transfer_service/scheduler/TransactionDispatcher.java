package ru.yandex.transfer_service.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yandex.bank.clients.accounts.AccountsClient;
import ru.yandex.bank.clients.accounts.dto.accounts.TransferMoneyRequest;
import ru.yandex.bank.clients.accounts.dto.accounts.TransferMoneyResponse;
import ru.yandex.bank.clients.accounts.exception.MoneyException;
import ru.yandex.bank.clients.exchange.ExchangeClient;
import ru.yandex.bank.clients.exchange.dto.Currency;
import ru.yandex.bank.clients.exchange.dto.ExchangeRateResponse;
import ru.yandex.transfer_service.mapper.TransactionMapper;
import ru.yandex.transfer_service.model.Transaction;
import ru.yandex.transfer_service.model.TransactionStatus;
import ru.yandex.transfer_service.repository.TransactionRepository;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TransactionDispatcher {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountsClient accountsClient;
    private final ExchangeClient exchangeClient;

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

    private Mono<TransferMoneyResponse> processTransaction(Transaction transaction) {
        return exchangeClient.readAll()
                .collectMap(ExchangeRateResponse::getCurrency, ExchangeRateResponse::getRate)
                .zipWith(readCurrenciesOfAccounts(transaction))
                .flatMap(tuple -> {
                    var rates = tuple.getT1();
                    var currencies = tuple.getT2();
                    return accountsClient.transferMoney(TransferMoneyRequest.builder()
                            .fromAccountId(transaction.getAccountId())
                            .fromMoneyAmount(transaction.getAmount())
                            .toAccountId(transaction.getReceiverAccountId())
                            .toMoneyAmount(exchangeMoney(transaction.getAmount(), currencies.getFirst(), currencies.getLast(), rates))
                            .build());
                });
    }

    private Mono<List<Currency>> readCurrenciesOfAccounts(Transaction transaction) {
        return accountsClient.readAccountById(transaction.getAccountId())
                .zipWith(accountsClient.readAccountById(transaction.getReceiverAccountId()))
                .map(tuple ->
                        List.of(Currency.valueOf(tuple.getT1().getCurrency()), Currency.valueOf(tuple.getT2().getCurrency())));
    }

    private Double exchangeMoney(Double amount, Currency from, Currency to, Map<Currency, Double> rates) {
        Double amountInRubles = amount / rates.get(from);
        return amountInRubles * rates.get(to);
    }
}
