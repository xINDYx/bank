package ru.yandex.accounts_service.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordChangeRequestDto {
    @Size(min = 8)
    private String password;

}
