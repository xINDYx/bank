package ru.yandex.front_ui.dto.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequestDTO {
    private String login;
    private String password;
}
