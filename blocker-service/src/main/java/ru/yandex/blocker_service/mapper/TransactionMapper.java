package ru.yandex.blocker_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.yandex.blocker_service.dto.DepositTransactionRequestDto;
import ru.yandex.blocker_service.dto.SelfTransferTransactionRequestDto;
import ru.yandex.blocker_service.dto.TransferAnotherUserTransactionRequestDto;
import ru.yandex.blocker_service.dto.WithdrawTransactionRequestDto;
import ru.yandex.blocker_service.model.DepositTransaction;
import ru.yandex.blocker_service.model.SelfTransferTransaction;
import ru.yandex.blocker_service.model.TransferToOtherUserTransaction;
import ru.yandex.blocker_service.model.WithdrawTransaction;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TransactionMapper {
    DepositTransaction map(DepositTransactionRequestDto transaction);

    WithdrawTransaction map(WithdrawTransactionRequestDto transaction);

    SelfTransferTransaction map(SelfTransferTransactionRequestDto transaction);

    TransferToOtherUserTransaction map(TransferAnotherUserTransactionRequestDto transaction);
}
