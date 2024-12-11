FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/device-management-0.0.1-SNAPSHOT.jar device-management.jar
EXPOSE 8080
CMD ["java", "-jar", "device-management.jar"]