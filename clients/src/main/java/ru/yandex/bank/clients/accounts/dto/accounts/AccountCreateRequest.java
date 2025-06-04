package ru.yandex.bank.clients.accounts.dto.accounts;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountCreateRequest {
    @NotNull
    private Long userId;
    @NotNull
    private String currency;

}
