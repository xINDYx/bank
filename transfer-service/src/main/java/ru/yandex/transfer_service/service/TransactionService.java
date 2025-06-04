package ru.yandex.transfer_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.transfer_service.dto.CreateTransactionRequestDTO;
import ru.yandex.transfer_service.dto.TransactionResponseDTO;
import ru.yandex.transfer_service.mapper.TransactionMapper;
import ru.yandex.transfer_service.model.TransactionType;
import ru.yandex.transfer_service.repository.TransactionRepository;

@Service
@RequiredArgsConstructor
public class TransactionService{
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;


    public Mono<TransactionResponseDTO> create(TransactionType transactionType, CreateTransactionRequestDTO request) {
        var transaction = transactionMapper.map(transactionType, request);
        return Mono.just(transaction)
                .map(transactionMapper::mapToDb)
                .flatMap(transactionRepository::save)
                .map(transactionMapper::map)
                .map(transactionMapper::map);
    }

}
