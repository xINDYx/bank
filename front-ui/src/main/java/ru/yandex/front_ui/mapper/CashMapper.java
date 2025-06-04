package ru.yandex.front_ui.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.yandex.bank.clients.cash.dto.CreateTransactionRequest;
import ru.yandex.bank.clients.cash.dto.TransactionResponse;
import ru.yandex.front_ui.dto.cash.CashTransactionResponseDTO;
import ru.yandex.front_ui.dto.cash.CreateCashTransactionRequestDTO;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CashMapper {
    CreateTransactionRequest map(CreateCashTransactionRequestDTO request);

    CashTransactionResponseDTO map(TransactionResponse response);
}
