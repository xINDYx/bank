FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN mvn -pl discovery-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/discovery-service/target/discovery-service-*.jar app.jar

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]