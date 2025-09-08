#!/bin/bash

# Build all services - CI/CD ready script
set -e

echo "🏗️  Healthcare Platform - Build All Services"
echo "=============================================="

# Configuration
BUILD_VERSION=${1:-"1.0-SNAPSHOT"}
SKIP_TESTS=${2:-false}
DOCKER_REGISTRY=${DOCKER_REGISTRY:-"healthcare"}
BUILD_START_TIME=$(date +%s)

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    local color=$1
    local message=$2
    echo -e "${color}${message}${NC}"
}

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Prerequisites check
print_status $BLUE "🔍 Checking prerequisites..."

if ! command_exists mvn; then
    print_status $RED "❌ Maven is not installed. Please install Maven first."
    exit 1
fi

if ! command_exists docker; then
    print_status $RED "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

print_status $GREEN "✅ All prerequisites satisfied"

# Build configuration
MAVEN_OPTS="-Dmaven.compiler.source=21 -Dmaven.compiler.target=21"
if [ "$SKIP_TESTS" = "true" ]; then
    MAVEN_OPTS="$MAVEN_OPTS -DskipTests"
    print_status $YELLOW "⚠️  Tests will be skipped"
else
    print_status $BLUE "🧪 Tests will be executed"
fi

# Services to build
SERVICES=(
    "discovery-server"
    "api-gateway" 
    "auth-service"
    "profile-service"
    "booking-service"
    "products"
    "notification-service"
    "swagger-aggregator"
)

# Clean and build parent project
print_status $BLUE "🧹 Cleaning and building parent project..."
mvn clean install $MAVEN_OPTS -q

if [ $? -ne 0 ]; then
    print_status $RED "❌ Parent project build failed"
    exit 1
fi

print_status $GREEN "✅ Parent project built successfully"

# Build individual services
successful_builds=0
failed_builds=0
build_results=()

for service in "${SERVICES[@]}"; do
    print_status $BLUE "🔨 Building $service..."
    
    if [ ! -d "$service" ]; then
        print_status $YELLOW "⚠️  Directory $service not found, skipping..."
        continue
    fi
    
    # Maven build
    start_time=$(date +%s)
    if (cd "$service" && mvn clean package $MAVEN_OPTS -q); then
        end_time=$(date +%s)
        duration=$((end_time - start_time))
        print_status $GREEN "✅ $service built successfully (${duration}s)"
        
        # Docker build
        print_status $BLUE "🐳 Building Docker image for $service..."
        if docker build -t "${DOCKER_REGISTRY}/${service}:${BUILD_VERSION}" -t "${DOCKER_REGISTRY}/${service}:latest" "$service" >/dev/null 2>&1; then
            print_status $GREEN "✅ Docker image built for $service"
            build_results+=("✅ $service - SUCCESS")
            ((successful_builds++))
        else
            print_status $RED "❌ Docker build failed for $service"
            build_results+=("❌ $service - DOCKER FAILED")
            ((failed_builds++))
        fi
    else
        print_status $RED "❌ Maven build failed for $service"
        build_results+=("❌ $service - MAVEN FAILED")
        ((failed_builds++))
    fi
done

# Build summary
BUILD_END_TIME=$(date +%s)
TOTAL_DURATION=$((BUILD_END_TIME - BUILD_START_TIME))

echo ""
print_status $BLUE "📊 Build Summary"
print_status $BLUE "================"
echo ""

for result in "${build_results[@]}"; do
    if [[ $result == *"SUCCESS"* ]]; then
        print_status $GREEN "$result"
    else
        print_status $RED "$result"
    fi
done

echo ""
print_status $BLUE "📈 Build Statistics:"
print_status $GREEN "✅ Successful builds: $successful_builds"
if [ $failed_builds -gt 0 ]; then
    print_status $RED "❌ Failed builds: $failed_builds"
fi
print_status $BLUE "⏱️  Total build time: ${TOTAL_DURATION}s"
print_status $BLUE "🏷️  Build version: $BUILD_VERSION"

# Docker images summary
echo ""
print_status $BLUE "🐳 Docker Images Created:"
docker images --filter "reference=${DOCKER_REGISTRY}/*:${BUILD_VERSION}" --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}\t{{.CreatedAt}}"

echo ""
if [ $failed_builds -eq 0 ]; then
    print_status $GREEN "🎉 All services built successfully!"
    print_status $BLUE "💡 Next steps:"
    print_status $BLUE "   • Start services: docker-compose up -d"
    print_status $BLUE "   • Check health: ./script/check-services.sh"
    print_status $BLUE "   • Generate docs: ./script/generate-unified-swagger.sh"
    exit 0
else
    print_status $RED "💥 Some builds failed. Please check the logs above."
    exit 1
fi