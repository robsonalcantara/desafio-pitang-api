FROM openjdk:17
WORKDIR /app
COPY ./target/*.jar ./rhp-painel-v2.jar
EXPOSE 8080

ENTRYPOINT java -jar rhp-painel-v2.jar