package ru.yandex.transfer_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.transfer_service.dto.NotificationEvent;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topics.notification-events}")
    private String notificationTopic;

    public Mono<Void> sendNotificationEvent(NotificationEvent event) {
        try {
            // Устанавливаем eventId и timestamp если не установлены
            if (event.getEventId() == null) {
                event.setEventId(UUID.randomUUID().toString());
            }
            if (event.getTimestamp() == null) {
                event.setTimestamp(LocalDateTime.now());
            }
            if (event.getCreatedAt() == null) {
                event.setCreatedAt(LocalDateTime.now());
            }

            String message = objectMapper.writeValueAsString(event);
            String key = event.getEventId();

            log.info("Отправка события уведомления в Kafka: eventId={}, topic={}, recipient={}", 
                    event.getEventId(), notificationTopic, event.getRecipient());

            return Mono.fromFuture(kafkaTemplate.send(notificationTopic, key, message))
                    .doOnSuccess(result -> {
                        log.info("Событие уведомления успешно отправлено в Kafka: eventId={}, topic={}, partition={}, offset={}", 
                                event.getEventId(), notificationTopic, result.getRecordMetadata().partition(), 
                                result.getRecordMetadata().offset());
                    })
                    .doOnError(error -> {
                        log.error("Ошибка при отправке события уведомления в Kafka: eventId={}, error={}", 
                                event.getEventId(), error.getMessage());
                    })
                    .then();

        } catch (Exception e) {
            log.error("Ошибка при сериализации события уведомления: eventId={}, error={}", 
                    event.getEventId(), e.getMessage(), e);
            return Mono.error(e);
        }
    }
} 