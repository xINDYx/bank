package ru.yandex.blocker_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class DecisionResponseDto {
    @JsonProperty("isBlocked")
    private boolean isBlocked;

}
