package ru.yandex.notifications_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.notifications_service.dto.CreateEmailNotificationRequestDTO;
import ru.yandex.notifications_service.dto.EmailNotificationResponseDTO;
import ru.yandex.notifications_service.dto.NotificationEvent;
import ru.yandex.notifications_service.mapper.NotificationMapper;
import ru.yandex.notifications_service.repository.EmailNotificationRepository;

@Service
@RequiredArgsConstructor
public class EmailNotificationService{
    private final EmailNotificationRepository emailNotificationRepository;
    private final NotificationMapper notificationMapper;

    public Mono<EmailNotificationResponseDTO> create(CreateEmailNotificationRequestDTO request) {
        return Mono.just(request)
                .map(notificationMapper::map)
                .flatMap(emailNotificationRepository::save)
                .map(notificationMapper::map);
    }

    public Mono<EmailNotificationResponseDTO> createFromEvent(NotificationEvent event) {
        return Mono.just(event)
                .map(this::convertEventToRequest)
                .map(notificationMapper::map)
                .flatMap(emailNotificationRepository::save)
                .map(notificationMapper::map);
    }

    private CreateEmailNotificationRequestDTO convertEventToRequest(NotificationEvent event) {
        CreateEmailNotificationRequestDTO request = new CreateEmailNotificationRequestDTO();
        request.setRecipient(event.getRecipient());
        request.setSubject(event.getSubject());
        request.setMessage(event.getMessage());
        return request;
    }
}
