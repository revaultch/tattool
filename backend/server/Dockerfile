#docker run --name qs-back -d -p 8888:8080 qarts-studio/backend:1.0
FROM openjdk:12-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
