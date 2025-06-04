package ru.yandex.exchange_service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.yandex.exchange_service.dto.ExchangeRateResponseDTO;
import ru.yandex.exchange_service.dto.UpdateExchangeRateRequestDTO;
import ru.yandex.exchange_service.model.ExchangeRate;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ExchangeMapper {
    ExchangeRateResponseDTO map(ExchangeRate exchangeRate);

    ExchangeRate map(UpdateExchangeRateRequestDTO request);
}
