# ==========================================

# Stage 1: Build the application using Gradle

# ==========================================

FROM gradle:8.5-jdk21-alpine AS build



# Set the working directory

WORKDIR /app



# Copy all project files into the container

COPY . .



# Grant execution rights to the gradlew script and build the JAR (skipping tests)

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x test



# ==========================================

# Stage 2: Run the application

# ==========================================

FROM eclipse-temurin:21-jdk-alpine



# Set the working directory

WORKDIR /app



# Copy ONLY the built JAR file from the 'build' stage

# Gradle typically outputs the Spring Boot fat jar to build/libs/

COPY --from=build /app/build/libs/*.jar app.jar



# Expose the port your Spring Boot app runs on

EXPOSE 8080



# Run the application

ENTRYPOINT ["java", "-jar", "app.jar"]