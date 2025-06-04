package ru.yandex.accounts_service.dto.account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TransferMoneyResponseDto {
    private boolean completed;

}
