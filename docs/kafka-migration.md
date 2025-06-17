# Миграция на Apache Kafka для уведомлений

## Обзор

Данный документ описывает миграцию системы уведомлений с REST API на Apache Kafka для обеспечения надежной доставки сообщений с гарантией "at least once".

## Архитектурные изменения

### До миграции
```
transfer-service ──REST──> notifications-service
cash-service ─────REST──> notifications-service
```

### После миграции
```
transfer-service ──Kafka──> notification-events ──Kafka──> notifications-service
cash-service ─────Kafka──> notification-events ──Kafka──> notifications-service
```

## Реализованные изменения

### 1. Notifications Service

#### Новые компоненты:
- **NotificationEvent** - DTO для Kafka событий
- **KafkaConfig** - конфигурация Kafka consumer
- **NotificationKafkaListener** - listener для обработки событий из Kafka

#### Конфигурация:
```yaml
spring:
  kafka:
    bootstrap-servers: bank-kafka:9092
    consumer:
      group-id: notifications-service-group
      auto-offset-reset: earliest
      enable-auto-commit: false
      isolation-level: read-committed
```

#### Стратегия обработки:
- **At least once delivery**: Подтверждение сообщений только после успешной обработки
- **Manual acknowledgment**: Ручное подтверждение для контроля доставки
- **Error handling**: Повторная обработка при ошибках

### 2. Transfer Service

#### Новые компоненты:
- **NotificationEvent** - DTO для Kafka событий
- **KafkaConfig** - конфигурация Kafka producer
- **NotificationKafkaService** - сервис для отправки событий в Kafka

#### Конфигурация:
```yaml
spring:
  kafka:
    bootstrap-servers: bank-kafka:9092
    producer:
      acks: all
      retries: 3
      enable-idempotence: true
```

#### Стратегия отправки:
- **Idempotent producer**: Предотвращение дублирования сообщений
- **At least once delivery**: Гарантия доставки с повторными попытками
- **Transactional messages**: Поддержка транзакций

### 3. Cash Service

Аналогичные изменения как в Transfer Service.

## Топики Kafka

### notification-events
- **Описание**: Топик для событий уведомлений
- **Партиции**: 3 (настраивается по окружениям)
- **Replication Factor**: 2-3 (в зависимости от окружения)
- **Retention**: 7 дней (prod), 3 дня (test), 1 день (dev)

### Структура сообщения:
```json
{
  "eventId": "uuid",
  "eventType": "TRANSACTION_NOTIFICATION",
  "recipient": "user@example.com",
  "subject": "Перевод выполнен успешно",
  "message": "Уважаемый Иван Иванов...",
  "accountId": "123",
  "transactionId": "456",
  "transactionType": "TRANSFER",
  "amount": 1000.00,
  "currency": "RUB",
  "status": "COMPLETED",
  "timestamp": "2024-01-01 12:00:00",
  "createdAt": "2024-01-01 12:00:00"
}
```

## Преимущества новой архитектуры

### 1. Надежность
- **At least once delivery**: Гарантия доставки сообщений
- **Persistent storage**: Сохранность данных при перезапуске
- **Fault tolerance**: Восстановление после сбоев

### 2. Масштабируемость
- **Horizontal scaling**: Возможность масштабирования потребителей
- **Load distribution**: Распределение нагрузки по партициям
- **Independent scaling**: Независимое масштабирование производителей и потребителей

### 3. Производительность
- **Asynchronous processing**: Асинхронная обработка уведомлений
- **Batch processing**: Возможность пакетной обработки
- **Reduced latency**: Снижение задержек при пиковых нагрузках

### 4. Мониторинг и отладка
- **Message tracing**: Возможность отслеживания сообщений
- **Dead letter queues**: Обработка неудачных сообщений
- **Metrics**: Метрики производительности и надежности

## Конфигурация по окружениям

### Development
- **Kafka Replicas**: 1
- **Replication Factor**: 1
- **Partitions**: 1
- **Retention**: 24 часа

### Test
- **Kafka Replicas**: 2
- **Replication Factor**: 2
- **Partitions**: 2
- **Retention**: 72 часа

### Production
- **Kafka Replicas**: 3
- **Replication Factor**: 3
- **Partitions**: 3
- **Retention**: 168 часов (7 дней)

## Мониторинг и алерты

### Метрики для отслеживания:
- **Consumer lag**: Отставание потребителей
- **Producer throughput**: Пропускная способность производителей
- **Error rates**: Частота ошибок
- **Message processing time**: Время обработки сообщений

### Алерты:
- **High consumer lag**: Высокое отставание потребителей
- **Producer errors**: Ошибки производителей
- **Consumer failures**: Сбои потребителей
- **Topic unavailability**: Недоступность топиков

## Troubleshooting

### Проблемы с доставкой:
1. Проверьте логи Kafka producer
2. Убедитесь в доступности топика
3. Проверьте конфигурацию producer

### Проблемы с обработкой:
1. Проверьте логи Kafka consumer
2. Убедитесь в корректности JSON
3. Проверьте настройки consumer group

### Проблемы с производительностью:
1. Мониторьте consumer lag
2. Настройте количество партиций
3. Оптимизируйте batch size

## Миграционный план

### Этап 1: Подготовка
- [x] Развертывание Kafka кластера
- [x] Создание топиков
- [x] Обновление конфигураций

### Этап 2: Разработка
- [x] Реализация Kafka producer в transfer-service
- [x] Реализация Kafka producer в cash-service
- [x] Реализация Kafka consumer в notifications-service

### Этап 3: Тестирование
- [ ] Unit тесты
- [ ] Integration тесты
- [ ] Load тесты
- [ ] Failover тесты

### Этап 4: Развертывание
- [ ] Развертывание в dev окружении
- [ ] Развертывание в test окружении
- [ ] Развертывание в prod окружении

### Этап 5: Мониторинг
- [ ] Настройка метрик
- [ ] Настройка алертов
- [ ] Мониторинг производительности 