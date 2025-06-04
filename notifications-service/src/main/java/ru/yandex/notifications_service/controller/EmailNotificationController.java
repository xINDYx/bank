package ru.yandex.notifications_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.yandex.notifications_service.dto.CreateEmailNotificationRequestDTO;
import ru.yandex.notifications_service.dto.EmailNotificationResponseDTO;
import ru.yandex.notifications_service.service.EmailNotificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class EmailNotificationController {
    private final EmailNotificationService emailNotificationService;

    @PostMapping("/email")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EmailNotificationResponseDTO> save(@RequestBody @Valid CreateEmailNotificationRequestDTO request) {
        return emailNotificationService.create(request);
    }
}
