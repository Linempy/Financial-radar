FROM openjdk:17-jdk-slim

WORKDIR /app

COPY AdminPanel-1.0-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]