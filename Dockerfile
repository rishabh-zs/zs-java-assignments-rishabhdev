# ==========================================
# Stage 1: Build the application using Gradle
# ==========================================
FROM gradle:jdk21-alpine AS build

# Set the working directory
WORKDIR /app

# Copy all project files into the container
COPY . .

# Build the JAR using the built-in gradle command (skipping tests)
RUN gradle clean build -x test

# ==========================================
# Stage 2: Run the application
# ==========================================
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy ONLY the built JAR file from the 'build' stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]