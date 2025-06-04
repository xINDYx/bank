FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN mvn -pl blocker-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/blocker-service/target/blocker-service.jar app.jar

EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]