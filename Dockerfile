FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests clean package
FROM eclipse-temurin:21-jre
WORKDIR /app
LABEL org.opencontainers.image.source="https://github.com/jetsonblack/speech"
RUN mkdir -p /data
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DATASOURCE_URL=jdbc:sqlite:/data/app.db?foreign_keys=on
ENV PORT=8080
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT} -jar /app/app.jar"]