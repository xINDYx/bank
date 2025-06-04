package ru.yandex.cash_service.mapper;


import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;
import ru.yandex.cash_service.dto.CreateTransactionRequestDTO;
import ru.yandex.cash_service.dto.TransactionResponseDTO;
import ru.yandex.cash_service.model.DepositTransaction;
import ru.yandex.cash_service.model.Transaction;
import ru.yandex.cash_service.model.TransactionType;
import ru.yandex.cash_service.model.WithdrawalTransaction;
import ru.yandex.cash_service.repository.dto.TransactionRepositoryDTO;
import ru.yandex.bank.clients.blocker.dto.DepositTransactionRequest;
import ru.yandex.bank.clients.blocker.dto.WithdrawalTransactionRequest;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TransactionMapper {
    public abstract TransactionResponseDTO map(Transaction transaction);

    public abstract Transaction map(TransactionType transactionType, CreateTransactionRequestDTO transaction);

    public abstract Transaction map(TransactionRepositoryDTO transaction);

    public abstract TransactionRepositoryDTO mapToDb(Transaction transaction);

    public abstract DepositTransactionRequest map(DepositTransaction depositTransaction);

    public abstract WithdrawalTransactionRequest map(WithdrawalTransaction depositTransaction);

    @ObjectFactory
    public Transaction createTransactionFromRequest(TransactionType transactionType, CreateTransactionRequestDTO request) {
        return transactionTypeToClass(transactionType);
    }

    @ObjectFactory
    public Transaction createTransactionFromDatabase(TransactionRepositoryDTO transaction) {
        return transactionTypeToClass(transaction.getTransactionType());
    }

    @AfterMapping
    protected void afterMappingResponse(Transaction transaction,
                                        @MappingTarget TransactionResponseDTO transactionResponseDTO) {
        transactionResponseDTO.setTransactionType(transactionClassToTransactionType(transaction));
    }

    @AfterMapping
    protected void afterMappingToRepo(Transaction transaction,
                                      @MappingTarget TransactionRepositoryDTO transactionRepositoryDTO) {
        transactionRepositoryDTO.setTransactionType(transactionClassToTransactionType(transaction));
    }

    protected Transaction transactionTypeToClass(TransactionType transactionType) {
        return switch (transactionType) {
            case DEPOSIT -> new DepositTransaction();
            case WITHDRAWAL -> new WithdrawalTransaction();
        };
    }

    protected TransactionType transactionClassToTransactionType(Transaction transaction) {
        var name = transaction.getClass().getSimpleName()
                .replace("Transaction", "")
                .toUpperCase();

        return TransactionType.valueOf(name);
    }
}
