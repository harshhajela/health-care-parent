#!/bin/bash

# Docker deployment script - CI/CD ready
set -e

echo "🚀 Healthcare Platform - Docker Deployment"
echo "=========================================="

# Configuration
OPERATION=${1:-"up"}

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_status() {
    local color=$1
    local message=$2
    echo -e "${color}${message}${NC}"
}

# Use single compose file for local development
COMPOSE_FILE="docker-compose.yml"
print_status $BLUE "🔧 Using local development configuration"

# Check if compose file exists
if [ ! -f "$COMPOSE_FILE" ]; then
    print_status $RED "❌ Compose file $COMPOSE_FILE not found"
    exit 1
fi

# No environment files needed for local development

# Execute operation
case $OPERATION in
    "up")
        print_status $BLUE "🚀 Starting all services..."
        docker-compose -f "$COMPOSE_FILE" up -d
        
        print_status $BLUE "⏳ Waiting for services to be healthy..."
        sleep 30
        
        print_status $BLUE "🔍 Checking service health..."
        docker-compose -f "$COMPOSE_FILE" ps
        
        echo ""
        print_status $GREEN "🎉 Deployment completed!"
        print_status $BLUE "📊 Service URLs:"
        print_status $BLUE "   • Discovery Server: http://localhost:8761"
        print_status $BLUE "   • API Gateway: http://localhost:8080"
        print_status $BLUE "   • Swagger UI: http://localhost:8090/swagger-ui.html"
        print_status $BLUE "   • Auth Service: http://localhost:8081"
        ;;
        
    "down")
        print_status $YELLOW "🛑 Stopping all services..."
        docker-compose -f "$COMPOSE_FILE" down
        print_status $GREEN "✅ All services stopped"
        ;;
        
    "restart")
        print_status $YELLOW "🔄 Restarting all services..."
        docker-compose -f "$COMPOSE_FILE" restart
        print_status $GREEN "✅ All services restarted"
        ;;
        
    "logs")
        SERVICE=${2:-""}
        if [ -n "$SERVICE" ]; then
            print_status $BLUE "📋 Showing logs for $SERVICE..."
            docker-compose -f "$COMPOSE_FILE" logs -f "$SERVICE"
        else
            print_status $BLUE "📋 Showing logs for all services..."
            docker-compose -f "$COMPOSE_FILE" logs -f
        fi
        ;;
        
    "status"|"ps")
        print_status $BLUE "📊 Service status:"
        docker-compose -f "$COMPOSE_FILE" ps
        echo ""
        print_status $BLUE "🔍 Health checks:"
        ./script/check-services.sh
        ;;
        
    "build")
        print_status $BLUE "🏗️  Building all Docker images..."
        docker-compose -f "$COMPOSE_FILE" build --parallel
        print_status $GREEN "✅ All images built successfully"
        ;;
        
    "clean")
        print_status $YELLOW "🧹 Cleaning up Docker resources..."
        docker-compose -f "$COMPOSE_FILE" down -v --remove-orphans
        
        # Remove unused images
        docker image prune -f
        
        # Remove unused volumes
        docker volume prune -f
        
        print_status $GREEN "✅ Cleanup completed"
        ;;
        
    *)
        print_status $RED "❌ Invalid operation: $OPERATION"
        print_status $YELLOW "💡 Available operations:"
        print_status $YELLOW "   • up       - Start all services"
        print_status $YELLOW "   • down     - Stop all services" 
        print_status $YELLOW "   • restart  - Restart all services"
        print_status $YELLOW "   • logs     - Show service logs (optionally specify service name)"
        print_status $YELLOW "   • status   - Show service status and health"
        print_status $YELLOW "   • build    - Build all Docker images"
        print_status $YELLOW "   • clean    - Clean up Docker resources"
        exit 1
        ;;
esac