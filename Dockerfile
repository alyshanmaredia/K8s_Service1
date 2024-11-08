FROM openjdk:17-jdk-alpine

LABEL authors="alishanali"

WORKDIR /outerapp

COPY target/outercontainer-0.0.1.jar outerapp.jar

COPY src/main/resources/application.properties /outerapp/config/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "outerapp.jar"]