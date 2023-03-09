FROM maven:3.6.3-jdk-11-slim AS build
FROM openjdk:11
EXPOSE 7007
WORKDIR /mail_server
COPY . /mail_server
ADD target/MailServer.jar MailServer.jar
ENTRYPOINT ["java","-jar","target/MailServer.jar"]