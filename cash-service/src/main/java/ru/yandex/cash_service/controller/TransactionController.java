package ru.yandex.cash_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.yandex.cash_service.dto.CreateTransactionRequestDTO;
import ru.yandex.cash_service.dto.TransactionResponseDTO;
import ru.yandex.cash_service.model.TransactionType;
import ru.yandex.cash_service.service.TransactionService;
import ru.yandex.cash_service.validators.ValidTransactionType;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/{type}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TransactionResponseDTO> create(@PathVariable("type") @ValidTransactionType String transactionType,
                                               @RequestBody @Valid CreateTransactionRequestDTO request) {
        var type = TransactionType.valueOf(transactionType.toUpperCase());
        return transactionService.create(type, request);
    }
}
