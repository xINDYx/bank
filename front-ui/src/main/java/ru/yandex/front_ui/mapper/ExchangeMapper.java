package ru.yandex.front_ui.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.yandex.bank.clients.exchange.dto.ExchangeRateResponse;
import ru.yandex.front_ui.dto.exchange.ExchangeRateResponseDTO;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ExchangeMapper {
    ExchangeRateResponseDTO map(ExchangeRateResponse response);
}
