FROM gradle:7.6.6-jdk17 AS builder

WORKDIR /workspace

COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY gradle.properties .
COPY gradlew .
COPY gradle/wrapper/gradle-wrapper.jar gradle/wrapper/
COPY gradle/wrapper/gradle-wrapper.properties gradle/wrapper/

COPY AdminPanel ./AdminPanel
COPY NotificationService ./NotificationService
COPY RuleEngine ./RuleEngine
COPY TransactionalService ./TransactionalService
COPY AuditService ./AuditService

RUN chmod +x gradlew

RUN ./gradlew :NotificationService:build -x test --no-daemon

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /workspace/NotificationService/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]