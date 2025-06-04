# Bank Microservices Application

Краткое описание и инструкции по сборке и запуску мультисервисного проекта «Банк».

---

## 🎯 Обзор

Это мультимодульное микросервисное приложение, реализующее функциональность простого виртуального банка:
- Регистрация и аутентификация пользователей (OAuth2/OIDC через Keycloak).
- Управление аккаунтами и счетами (Accounts Service).
- Внесение/снятие виртуальных денег (Cash Service).
- Переводы между собственными счетами и на счета других пользователей (Transfer Service).
- Конвертация валют (Exchange Service) и генерация курсов (Exchange Generator Service).
- Блокировка подозрительных операций (Blocker Service).
- Отправка уведомлений (Notification Service).
- FrontUI (Thymeleaf) с HTML-интерфейсом.
- Gateway API и Eureka Discovery.
- Внешний конфиг через Spring Cloud Config.
- Хранение данных в PostgreSQL (R2DBC/JDBC), миграции Liquibase.
- Все сервисы упакованы в Docker, запускаются через Docker Compose.

---

## 📦 Структура проекта



---

## 🛠 Технологии

- **Язык & Фреймворк:** Java 21, Spring Boot 3 (WebFlux/WebMVC, Security, Data R2DBC, Data JDBC).
- **База данных:** PostgreSQL (каждый сервис своя схема), миграции Liquibase.
- **Discovery & Gateway:** Spring Cloud Eureka, Spring Cloud Gateway.
- **Config:** Spring Cloud Config Server (config-service).
- **Auth:** OAuth 2.0/OIDC с Keycloak (Client Credentials между сервисами, login/password для пользователей).
- **Docker:** Каждый сервис упакован в отдельный Docker Image, а весь проект собирается через `docker-compose.yml`.

---

## ⚙️ Предварительные требования

- JDK 21
- Maven 3.8+
- Docker 20+ & Docker Compose

---

## 🚀 Сборка и запуск локально

1. **Клонировать репозиторий:**
   ```bash
   git clone https://github.com/xINDYx/bank.git
   cd bank
   ```

2. **Собрать все модули Maven:**
   ```bash
   mvn clean package -DskipTests
   ```

3. **Настроить Keycloak:**
   ```bash
   http://localhost:8090
   ```

4. **Запустить все сервисы через Docker Compose:**
   ```bash
   docker-compose up -d
   ```
