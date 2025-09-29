# Single stage - build and run in same image (larger but simpler)
FROM maven:3.8.6-openjdk-17

WORKDIR /app

# Copy entire project
COPY . .

# Build the application
RUN mvn clean package -DskipTests

EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/*.jar"]