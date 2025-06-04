package ru.yandex.bank.clients.accounts.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String email;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

}
