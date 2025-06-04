package ru.yandex.accounts_service.dto.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FindByLoginRequestDto {
    private String login;

}
