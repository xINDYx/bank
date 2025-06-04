FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN mvn -pl cash-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/cash-service/target/cash-service.jar app.jar

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]