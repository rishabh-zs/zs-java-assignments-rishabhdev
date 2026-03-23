# Use a lightweight Java 21 environment
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the pre-built JAR file that GitHub Actions already compiled!
# (The GitHub Actions SCP step places it exactly in this folder structure)
COPY build/libs/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]