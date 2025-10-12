FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/petinder-back-1.0.0.jar petinder-back-1.0.0.jar
EXPOSE 8080
CMD ["java", "-jar", "petinder-back-1.0.0.jar"]