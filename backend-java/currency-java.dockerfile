FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} currency-java.jar
ENTRYPOINT ["java","-jar","/currency-java.jar","--server.port=8081"]
EXPOSE 8081