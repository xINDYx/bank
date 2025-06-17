package ru.yandex.notifications_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ru.yandex.notifications_service.dto.NotificationEvent;
import ru.yandex.notifications_service.service.EmailNotificationService;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaListener {

    private final EmailNotificationService emailNotificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "${kafka.topics.notification-events}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleNotificationEvent(
            ConsumerRecord<String, String> record,
            Acknowledgment acknowledgment,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset) {
        
        try {
            log.info("Получено сообщение из Kafka: topic={}, partition={}, offset={}, key={}", 
                    topic, partition, offset, record.key());
            
            String message = record.value();
            log.debug("Содержимое сообщения: {}", message);
            
            NotificationEvent event = objectMapper.readValue(message, NotificationEvent.class);
            log.info("Обработка события уведомления: eventId={}, eventType={}, recipient={}", 
                    event.getEventId(), event.getEventType(), event.getRecipient());
            
            // Создаем уведомление из события
            emailNotificationService.createFromEvent(event)
                    .doOnSuccess(result -> {
                        log.info("Уведомление успешно создано: id={}", result.getId());
                        acknowledgment.acknowledge();
                    })
                    .doOnError(error -> {
                        log.error("Ошибка при создании уведомления: eventId={}, error={}", 
                                event.getEventId(), error.getMessage());
                        // Не подтверждаем сообщение при ошибке для повторной обработки
                    })
                    .subscribe();
                    
        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения Kafka: topic={}, partition={}, offset={}, error={}", 
                    topic, partition, offset, e.getMessage(), e);
            // Не подтверждаем сообщение при ошибке для повторной обработки
        }
    }
} 