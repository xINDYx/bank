package ru.yandex.notifications_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmailNotificationResponseDTO {
    private Long id;
    private String recipient;
    private String subject;
    private String message;
    private Boolean sent;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
