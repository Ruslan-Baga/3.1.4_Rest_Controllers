FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY /target/spring-boot_security-demo-0.0.1-SNAPSHOT.jar /app/demo.jar
ENTRYPOINT ["java", "-jar","demo.jar"]