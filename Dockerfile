# Runtime only - we build the JAR in GitHub Actions
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the pre-built JAR from GitHub Actions
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]