package ru.yandex.exchange_generator_service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.yandex.bank.clients.exchange.dto.UpdateExchangeRateRequest;
import ru.yandex.exchange_generator_service.model.ExchangeRate;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ExchangeMapper {

    UpdateExchangeRateRequest map(ExchangeRate exchangeRate);
}
