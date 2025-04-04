FROM openjdk:21-jdk-slim
WORKDIR /app
COPY ./target/*.jar ./desafio-pitang-api-v1.jar
EXPOSE 8080

ENTRYPOINT java -jar desafio-pitang-api-v1.jar