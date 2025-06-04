
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-jammy
VOLUME /config-repo

COPY --from=builder /app/config-service/target/config-server.jar config-server.jar
ENTRYPOINT ["java", "-jar", "/config-server.jar"]