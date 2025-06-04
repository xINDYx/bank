FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN mvn -pl transfer-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/transfer-service/target/transfer-service.jar app.jar

EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]