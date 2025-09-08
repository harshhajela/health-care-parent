# booking-service Dockerfile
FROM eclipse-temurin:21-jre-alpine

# Add metadata
LABEL service="booking-service"
LABEL version="1.0-SNAPSHOT"
LABEL description="booking-service microservice"

# Install wget for health checks
RUN apk add --no-cache wget

# Create non-root user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/booking-service-1.0-SNAPSHOT.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8083/actuator/health || exit 1

# Expose port
EXPOSE 8083

# Run the application with optimized JVM settings
ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseG1GC", \
    "-XX:+UseContainerSupport", \
    "-jar", "app.jar"]
