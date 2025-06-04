package ru.yandex.cash_service.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;
import ru.yandex.cash_service.model.TransactionStatus;
import ru.yandex.cash_service.model.TransactionType;

import java.time.LocalDateTime;

@Getter
@Setter
@Table("transactions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRepositoryDTO {
    @Id
    private Long id;
    private Long accountId;
    private Double amount;
    private TransactionStatus transactionStatus;
    private Boolean notificationSent;
    private TransactionType transactionType;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
