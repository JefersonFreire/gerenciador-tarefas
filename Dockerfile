# Fase de build
FROM maven:3.9.9-eclipse-temurin-21-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080:8080
