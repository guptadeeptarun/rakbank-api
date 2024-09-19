FROM eclipse-temurin:17-jdk-alpine
MAINTAINER RakBank
ARG JAR_FILE_PATH
COPY target/userservice-*.jar userservice.jar
ENTRYPOINT ["java","-jar","/userservice.jar"]
EXPOSE 8080
