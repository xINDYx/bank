package ru.yandex.blocker_service.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public abstract class Transaction {
    private Long accountId;
    private Double money;
    private LocalDateTime createdAt;

}
