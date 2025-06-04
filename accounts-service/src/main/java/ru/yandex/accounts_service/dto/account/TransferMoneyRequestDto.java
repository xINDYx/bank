package ru.yandex.accounts_service.dto.account;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransferMoneyRequestDto {
    private Long fromAccountId;
    private Double fromAmount;
    private Long toAccountId;
    private Double toAmount;


}
