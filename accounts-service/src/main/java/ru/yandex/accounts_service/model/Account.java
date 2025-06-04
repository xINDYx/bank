package ru.yandex.accounts_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class Account {
    @Id
    private Long id;
    @Column("user_id")
    private Long userId;
    private Currency currency;
    private Double amount;
    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column("modified_at")
    private LocalDateTime modifiedAt;

    public Account(Long userId, Currency currency) {
        this.amount = 0.0;
        this.userId = userId;
        this.currency = currency;
    }

}
