FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN mvn -pl notifications-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/notifications-service/target/notifications-service.jar app.jar

EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]