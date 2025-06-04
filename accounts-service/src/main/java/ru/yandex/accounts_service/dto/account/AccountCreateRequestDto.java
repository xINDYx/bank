package ru.yandex.accounts_service.dto.account;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountCreateRequestDto {
    private Long userId;
    private String currency;

}
