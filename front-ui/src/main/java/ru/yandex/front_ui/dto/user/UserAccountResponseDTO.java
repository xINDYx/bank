package ru.yandex.front_ui.dto.user;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.front_ui.dto.account.AccountResponseDTO;

import java.util.List;

@Getter
@Setter
public class UserAccountResponseDTO {
    private Long id;
    private String surname;
    private String name;
    private List<AccountResponseDTO> accounts;
}
