FROM openjdk:17-jdk-alpine

LABEL authors="alishanali"
RUN apt-get update && apt-get install -y \
    vim \
    bash-completion \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /outerapp

COPY target/outercontainer-0.0.1.jar outerapp.jar

COPY src/main/resources/application.properties /outerapp/config/

ENTRYPOINT ["java", "-jar", "outerapp.jar"]