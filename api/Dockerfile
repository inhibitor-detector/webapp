# Usa la imagen oficial de Maven con Java 21
FROM maven:3.8.8-eclipse-temurin-21-alpine

RUN apk update && apk add --no-cache git

# Clona el proyecto de GitHub
WORKDIR /app
RUN git clone https://github.com/inhibitor-detector/webapp.git

# Define el directorio de trabajo dentro del contenedor
WORKDIR /app/webapp/api

CMD mvn clean package && java -jar ./webapp/target/webapp-1.0-SNAPSHOT.jar