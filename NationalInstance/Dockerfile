FROM maven:3.6.3-jdk-11-slim AS build
FROM openjdk:11
EXPOSE 7008
WORKDIR /national_javabackend
COPY . /national_javabackend
ADD target/NationalInstance.jar NationalInstance.jar
ENTRYPOINT ["java","-jar","target/NationalInstance.jar"]