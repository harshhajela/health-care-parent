# Use the official OpenJDK 17 image as the base image
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the host to the container's working directory
COPY target/care-provider-*.jar /app/care-provider-service.jar

# Expose the port that your Spring Boot app will listen on
EXPOSE 8082

# Define the command to run your Spring Boot application
CMD ["java", "-jar", "care-provider-service.jar"]
