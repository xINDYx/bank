package ru.yandex.front_ui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.bank.clients.cash.CashClient;
import ru.yandex.bank.clients.cash.dto.TransactionType;
import ru.yandex.front_ui.dto.cash.CashTransactionResponseDTO;
import ru.yandex.front_ui.dto.cash.CreateCashTransactionRequestDTO;
import ru.yandex.front_ui.mapper.CashMapper;

@Service
@RequiredArgsConstructor
public class CashService {
    private final CashClient cashClient;
    private final CashMapper cashMapper;

    public Mono<CashTransactionResponseDTO> deposit(CreateCashTransactionRequestDTO request) {
        return cashClient.createTransaction(TransactionType.DEPOSIT, cashMapper.map(request))
                .map(cashMapper::map);
    }

    public Mono<CashTransactionResponseDTO> withdrawal(CreateCashTransactionRequestDTO request) {
        return cashClient.createTransaction(TransactionType.WITHDRAWAL, cashMapper.map(request))
                .map(cashMapper::map);
    }
}
