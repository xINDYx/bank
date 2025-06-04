FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN mvn -pl exchange-generator-service clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/exchange-generator-service/target/exchange-generator-service.jar app.jar

EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]
