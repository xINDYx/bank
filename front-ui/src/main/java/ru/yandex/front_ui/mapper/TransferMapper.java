package ru.yandex.front_ui.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.yandex.bank.clients.transfer.dto.CreateTransactionRequest;
import ru.yandex.bank.clients.transfer.dto.TransactionResponse;
import ru.yandex.front_ui.dto.transfer.CreateTransactionRequestDTO;
import ru.yandex.front_ui.dto.transfer.TransactionResponseDTO;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TransferMapper {
    CreateTransactionRequest map(CreateTransactionRequestDTO request);

    TransactionResponseDTO map(TransactionResponse response);
}
