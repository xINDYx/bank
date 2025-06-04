package ru.yandex.cash_service.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.yandex.cash_service.model.TransactionStatus;
import ru.yandex.cash_service.repository.dto.TransactionRepositoryDTO;

import java.util.List;

@Repository
public interface TransactionRepository extends ReactiveCrudRepository<TransactionRepositoryDTO, Long> {
    Flux<TransactionRepositoryDTO> findByTransactionStatus(TransactionStatus transactionStatus);

    Flux<TransactionRepositoryDTO> findByTransactionStatusInAndNotificationSent(List<TransactionStatus> transactionStatuses, boolean notificationSent);
}
