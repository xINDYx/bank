# Развертывание Apache Kafka в Kubernetes

## Обзор

Apache Kafka развертывается в Kubernetes с использованием Helm чартов Bitnami. Конфигурация адаптирована для разных окружений (dev, test, prod) с соответствующими настройками ресурсов и репликации.

## Архитектура

### Компоненты
- **Kafka Broker**: Основной компонент для обработки сообщений
- **Zookeeper**: Координация кластера Kafka
- **Kafka Init Job**: Инициализация топиков при развертывании

### Топики
Следующие топики создаются автоматически:
- `account-events` - события аккаунтов
- `transaction-events` - события транзакций
- `exchange-rate-events` - события курсов валют
- `notification-events` - события уведомлений
- `blocker-events` - события блокировок

## Конфигурация по окружениям

### Development (dev)
- **Kafka Replicas**: 1
- **Zookeeper Replicas**: 1
- **Storage**: 4Gi для Kafka, 2Gi для Zookeeper
- **Replication Factor**: 1
- **Partitions**: 1
- **Retention**: 24 часа

### Test
- **Kafka Replicas**: 2
- **Zookeeper Replicas**: 1
- **Storage**: 6Gi для Kafka, 3Gi для Zookeeper
- **Replication Factor**: 2
- **Partitions**: 2
- **Retention**: 72 часа

### Production (prod)
- **Kafka Replicas**: 3
- **Zookeeper Replicas**: 1
- **Storage**: 10Gi для Kafka, 5Gi для Zookeeper
- **Replication Factor**: 3
- **Partitions**: 3
- **Retention**: 168 часов (7 дней)

## Развертывание

### 1. Обновление зависимостей Helm
```bash
helm dependency update helm/
```

### 2. Развертывание в dev окружении
```bash
helm install bank-dev helm/ -f helm/values-dev.yaml -n dev --create-namespace
```

### 3. Развертывание в test окружении
```bash
helm install bank-test helm/ -f helm/values-test.yaml -n test --create-namespace
```

### 4. Развертывание в prod окружении
```bash
helm install bank-prod helm/ -f helm/values-prod.yaml -n prod --create-namespace
```

## Подключение микросервисов

Микросервисы подключаются к Kafka через конфигурацию Spring Boot:

```yaml
spring:
  kafka:
    bootstrap-servers: bank-kafka:9092
    consumer:
      group-id: service-name-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      acks: all
      retries: 3
```

## Мониторинг и управление

### Проверка статуса Kafka
```bash
kubectl get pods -n <namespace> -l app.kubernetes.io/name=kafka
```

### Просмотр логов Kafka
```bash
kubectl logs -n <namespace> -l app.kubernetes.io/name=kafka
```

### Подключение к Kafka для управления топиками
```bash
kubectl exec -it -n <namespace> <kafka-pod-name> -- kafka-topics.sh --bootstrap-server localhost:9092 --list
```

### Создание нового топика
```bash
kubectl exec -it -n <namespace> <kafka-pod-name> -- kafka-topics.sh \
  --bootstrap-server localhost:9092 \
  --create \
  --topic new-topic \
  --partitions 3 \
  --replication-factor 2
```

## Персистентность данных

Данные Kafka сохраняются в Persistent Volumes, что обеспечивает:
- Сохранность данных при перезапуске подов
- Восстановление данных после сбоев
- Возможность масштабирования кластера

## Безопасность

- Kafka доступна только внутри кластера Kubernetes
- Внешний доступ отключен по умолчанию
- Аутентификация и авторизация настраиваются через Spring Security

## Troubleshooting

### Проблемы с подключением
1. Проверьте, что Kafka поды запущены
2. Убедитесь, что сервис `bank-kafka` доступен
3. Проверьте конфигурацию bootstrap-servers

### Проблемы с топиками
1. Проверьте логи kafka-init job
2. Убедитесь, что Zookeeper работает корректно
3. Проверьте права доступа к топикам

### Проблемы с производительностью
1. Мониторьте использование ресурсов
2. Настройте количество партиций и реплик
3. Оптимизируйте настройки retention 