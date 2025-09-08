#!/bin/bash

# Generate Dockerfiles for all services
set -e

echo "🐳 Generating Dockerfiles for all services..."

# Service configurations: service_name|port|jar_name
SERVICES=(
    "api-gateway|8080|api-gateway-1.0-SNAPSHOT.jar"
    "profile-service|8082|profile-service-1.0-SNAPSHOT.jar"
    "booking-service|8083|booking-service-1.0-SNAPSHOT.jar"
    "products|8084|products-0.0.1-SNAPSHOT.jar"
    "notification-service|8085|notification-service-1.0-SNAPSHOT.jar"
    "swagger-aggregator|8090|swagger-aggregator-1.0-SNAPSHOT.jar"
)

# Function to create Dockerfile
create_dockerfile() {
    local service_name="$1"
    local port="$2"
    local jar_name="$3"
    
    cat > "${service_name}/Dockerfile" << EOF
# ${service_name} Dockerfile
FROM eclipse-temurin:21-jre-alpine

# Add metadata
LABEL service="${service_name}"
LABEL version="1.0-SNAPSHOT"
LABEL description="${service_name} microservice"

# Install wget for health checks
RUN apk add --no-cache wget

# Create non-root user for security
RUN addgroup -g 1001 -S appgroup && \\
    adduser -u 1001 -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY target/${jar_name} app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \\
  CMD wget --no-verbose --tries=1 --spider http://localhost:${port}/actuator/health || exit 1

# Expose port
EXPOSE ${port}

# Run the application with optimized JVM settings
ENTRYPOINT ["java", \\
    "-Djava.security.egd=file:/dev/./urandom", \\
    "-XX:MaxRAMPercentage=75.0", \\
    "-XX:+UseG1GC", \\
    "-XX:+UseContainerSupport", \\
    "-jar", "app.jar"]
EOF
    
    echo "✅ Created Dockerfile for ${service_name}"
}

# Create Dockerfiles for all services
for service_info in "${SERVICES[@]}"; do
    IFS='|' read -r service_name port jar_name <<< "$service_info"
    
    if [ -d "$service_name" ]; then
        create_dockerfile "$service_name" "$port" "$jar_name"
    else
        echo "⚠️  Directory $service_name not found, skipping..."
    fi
done

echo ""
echo "🎯 Summary:"
echo "✅ discovery-server - Already exists"
echo "✅ auth-service - Already updated"

for service_info in "${SERVICES[@]}"; do
    IFS='|' read -r service_name port jar_name <<< "$service_info"
    if [ -f "${service_name}/Dockerfile" ]; then
        echo "✅ ${service_name} - Created"
    else
        echo "❌ ${service_name} - Failed"
    fi
done

echo ""
echo "🐳 All Dockerfiles generated successfully!"
echo "💡 Next: Create docker-compose files for orchestration"