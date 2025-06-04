package ru.yandex.front_ui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.bank.clients.transfer.TransferClient;
import ru.yandex.bank.clients.transfer.dto.TransactionResponse;
import ru.yandex.bank.clients.transfer.dto.TransactionType;
import ru.yandex.front_ui.dto.transfer.CreateTransactionRequestDTO;
import ru.yandex.front_ui.mapper.TransferMapper;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final TransferClient transferClient;
    private final TransferMapper transferMapper;

    public Mono<TransactionResponse> selfTransfer(CreateTransactionRequestDTO request) {
        return transferClient
                .createTransaction(TransactionType.SELF_TRANSFER, transferMapper.map(request));
    }

    public Mono<TransactionResponse> transferToOtherUser(CreateTransactionRequestDTO request) {
        return transferClient
                .createTransaction(TransactionType.TRANSFER_TO_OTHER_USER, transferMapper.map(request));
    }
}
