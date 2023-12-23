# Use the official OpenJDK 21-slim image as the base image
FROM openjdk:21-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the host to the container's working directory
COPY target/discovery-server-*.jar /app/discovery-server.jar

# Expose the port that your Spring Boot app will listen on
EXPOSE 8761

# Define the command to run your Spring Boot application
CMD ["java", "-jar", "discovery-server.jar"]
