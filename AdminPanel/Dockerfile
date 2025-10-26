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

# Собираем только нужный модуль
RUN ./gradlew :AdminPanel:build -x test --no-daemon

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /workspace/AdminPanel/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]