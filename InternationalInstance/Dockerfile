FROM maven:3.6.3-jdk-11-slim AS build
FROM openjdk:11
EXPOSE 7009
WORKDIR /international_javabackend
COPY . /international_javabackend
ADD target/InternationalInstance.jar InternationalInstance.jar
ENTRYPOINT ["java","-jar","target/InternationalInstance.jar"]