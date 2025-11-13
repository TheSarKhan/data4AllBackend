FROM openjdk:17-jdk-slim
COPY target/backend-0.0.1-SNAPSHOT.jar dataportal.jar

CMD ["java", "-jar","dataportal.jar"]
