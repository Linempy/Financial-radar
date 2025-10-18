## TODO ДОДЕЛАТЬ!!!

FROM gradle:7-jdk17 AS build
WORKDIR /workspace

COPY build.gradle.kts .
COPY src ./src

RUN gradle build -x test --no-daemon

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /workspace/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]