package ru.yandex.exchange_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ru.yandex.exchange_service.dto.ExchangeRateResponseDTO;
import ru.yandex.exchange_service.service.ExchangeService;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateKafkaListener {
    private final ExchangeService exchangeService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "${kafka.topics.exchange-rate-events}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleExchangeRateEvent(
            ConsumerRecord<String, String> record,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset) {
        try {
            log.info("Получено сообщение курса валюты из Kafka: topic={}, partition={}, offset={}, key={}",
                    topic, partition, offset, record.key());
            String message = record.value();
            log.debug("Содержимое сообщения: {}", message);
            ExchangeRateResponseDTO event = objectMapper.readValue(message, ExchangeRateResponseDTO.class);
            // Обновляем курс валюты в сервисе
            exchangeService.updateExchangeRateFromEvent(event);
        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения Kafka: topic={}, partition={}, offset={}, error={}",
                    topic, partition, offset, e.getMessage(), e);
        }
    }
} 