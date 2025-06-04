package ru.yandex.accounts_service.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class UserResponseDto {
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDateTime birthDate;

}
