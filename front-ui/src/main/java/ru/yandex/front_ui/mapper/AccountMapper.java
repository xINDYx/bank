package ru.yandex.front_ui.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.yandex.bank.clients.accounts.dto.accounts.AccountResponse;
import ru.yandex.bank.clients.accounts.dto.accounts.CreateAccountRequest;
import ru.yandex.front_ui.dto.account.AccountResponseDTO;
import ru.yandex.front_ui.dto.account.CreateAccountRequestDTO;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AccountMapper {
    CreateAccountRequest map(CreateAccountRequestDTO request);

    AccountResponseDTO map(AccountResponse accountResponse);
}
