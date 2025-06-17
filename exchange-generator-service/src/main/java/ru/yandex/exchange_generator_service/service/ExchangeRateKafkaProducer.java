package ru.yandex.exchange_generator_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.exchange_generator_service.dto.ExchangeRateEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateKafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topics.exchange-rate-events}")
    private String topic;

    public Mono<Void> sendExchangeRateEvent(ExchangeRateEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            // Используем фиксированный ключ для ordered delivery (одна партиция)
            String key = "exchange-rate";
            log.info("Отправка события курса валюты в Kafka: {}", message);
            kafkaTemplate.send(topic, key, message);
            // At most once: не ждем подтверждения
            return Mono.empty();
        } catch (Exception e) {
            log.error("Ошибка сериализации события курса валюты: {}", e.getMessage(), e);
            return Mono.error(e);
        }
    }
} 