package ru.yandex.accounts_service.dto.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.accounts_service.model.Currency;

import java.time.LocalDateTime;

@Setter
@Getter
public class AccountResponseDto {
    private Long id;
    private Long userId;
    private Currency currency;
    private Double amount;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDateTime modifiedAt;

}
