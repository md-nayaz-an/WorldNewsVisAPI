FROM maven:3.9-eclipse-temurin-20-alpine AS build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn clean install -U -Dmaven.test.failure.ignore=true

FROM eclipse-temurin:20-jdk-alpine
VOLUME /tmp
COPY --from=build target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080