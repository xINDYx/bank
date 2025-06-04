package ru.yandex.bank.clients.accounts.dto.accounts;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserAccountsResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private List<AccountResponse> accounts;

}
