package ru.yandex.bank.clients.accounts.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordChangeRequest {
    @Size(min = 8)
    private String password;

}
