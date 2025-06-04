package ru.yandex.accounts_service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.accounts_service.dto.account.AccountResponseDto;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountsResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private List<AccountResponseDto> accounts;

}
