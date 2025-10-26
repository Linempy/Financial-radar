FROM gradle:7.6.6-jdk17 AS build

COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY gradle ./gradle

COPY gradle/plugins.gradle.kts ./gradle/

COPY TransactionalService ./TransactionalService

RUN gradle :TransactionalService:build -x test --no-daemon

FROM openjdk:17-jdk-slim

COPY --from=build /home/gradle/TransactionalService/build/libs/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app.jar"]