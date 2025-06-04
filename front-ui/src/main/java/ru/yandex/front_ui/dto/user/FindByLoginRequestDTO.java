package ru.yandex.front_ui.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FindByLoginRequestDTO {
    private String login;
}
