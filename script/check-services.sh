#!/bin/bash

# Service Health Check Script for macOS
echo "🏥 Healthcare Platform - Service Health Check"
echo "============================================="

# Service configurations
declare -a SERVICES=(
    "Discovery Server|http://localhost:8761|/actuator/health"
    "API Gateway|http://localhost:8080|/actuator/health"
    "Swagger Aggregator|http://localhost:8090|/actuator/health"
    "Auth Service|http://localhost:8081|/actuator/health"
    "Profile Service|http://localhost:8082|/actuator/health"
    "Booking Service|http://localhost:8083|/actuator/health"
    "Products Service|http://localhost:8084|/actuator/health"
    "Notification Service|http://localhost:8085|/actuator/health"
)

running_count=0
total_count=${#SERVICES[@]}

echo "🔍 Checking $total_count services..."
echo ""

for service_info in "${SERVICES[@]}"; do
    IFS='|' read -r name url endpoint <<< "$service_info"
    
    echo -n "📊 $name ($url): "
    
    if curl -s --connect-timeout 3 --max-time 5 "$url$endpoint" > /dev/null 2>&1; then
        echo "✅ RUNNING"
        ((running_count++))
        
        # Check if swagger docs are available
        if curl -s --connect-timeout 3 --max-time 5 "$url/v3/api-docs" | grep -q '"openapi"' 2>/dev/null; then
            echo "   📚 Swagger docs: ✅ Available"
        else
            echo "   📚 Swagger docs: ❌ Not available"
        fi
        
        # Check if Swagger UI is available
        if curl -s --connect-timeout 3 --max-time 5 "$url/swagger-ui.html" > /dev/null 2>&1; then
            echo "   🌐 Swagger UI: ✅ http://localhost:${url##*:}/swagger-ui.html"
        fi
        
    else
        echo "❌ NOT RUNNING"
    fi
    echo ""
done

echo "📈 Summary: $running_count/$total_count services are running"
echo ""

if [ $running_count -eq 0 ]; then
    echo "⚠️  No services are running!"
    echo "💡 Start services first using: ./script/start-services.sh"
elif [ $running_count -lt $total_count ]; then
    echo "⚠️  Some services are not running"
    echo "💡 Check the logs and make sure all services started correctly"
else
    echo "🎉 All services are running!"
    echo "💡 You can now run: ./script/generate-unified-swagger.sh"
fi

echo ""
echo "🔧 Quick service URLs:"
echo "   - Discovery: http://localhost:8761"
echo "   - API Gateway: http://localhost:8080"
echo "   - Swagger Aggregator: http://localhost:8090/swagger-ui.html"
echo "   - Individual Swagger UIs: http://localhost:<port>/swagger-ui.html"