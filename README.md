# Банковская система микросервисов

## Описание
Банковская система, реализованная в виде микросервисов на базе Kubernetes и Helm. Система включает сервисы для управления счетами, операциями с наличными, обменом валют, уведомлениями и другими банковскими операциями.

## Микросервисы
- `accounts-service` - управление счетами
- `cash-service` - операции с наличными
- `exchange-service` - обмен валют
- `exchange-generator-service` - генерация курсов
- `notifications-service` - уведомления
- `transfer-service` - переводы
- `blocker-service` - блокировка операций
- `front-ui` - пользовательский интерфейс

## Технологии
- Java 17, Spring Boot 3.x
- Kubernetes, Helm
- PostgreSQL (StatefulSets)
- Keycloak (OAuth2)
- Jenkins (CI/CD)

## Инфраструктура
- **Kubernetes**: Minikube/Kind/Colima
- **Namespaces**: bank-dev, bank-test, bank-prod
- **Service Discovery**: Kubernetes DNS
- **API Gateway**: Kubernetes Ingress
- **Конфигурация**: ConfigMaps и Secrets
- **База данных**: PostgreSQL в StatefulSets
- **Авторизация**: Keycloak через Helm

## Развертывание
### Требования
- Kubernetes кластер
- Helm 3.x
- kubectl
- Docker
- Jenkins


## CI/CD
- Jenkins пайплайны в директории `ci/`
- Этапы: валидация, сборка, тестирование, развертывание

## Локальная разработка
```bash
cd docs
docker-compose up
```
