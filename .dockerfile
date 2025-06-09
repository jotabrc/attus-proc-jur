FROM eclipse-temurin:21-jre-alpine
WORKDIR /proc-jur-service
COPY target/*.jar proc-jur-service.jar
ENTRYPOINT ["java", "-jar", "proc-jur-service.jar"]

EXPOSE 8080