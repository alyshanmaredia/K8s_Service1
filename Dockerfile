FROM openjdk:17-jdk-alpine

LABEL authors="alishanali"

WORKDIR /outercontainer

COPY target/outercontainer-0.0.1.jar outercontainer.jar

COPY src/main/resources/application.properties /outercontainer/config/
EXPOSE 6000
ENTRYPOINT ["java", "-jar", "outercontainer.jar","--spring.config.location=/outercontainer/config/application.properties"]