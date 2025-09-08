#!/bin/bash

# Healthcare Platform Unified Swagger Generator
# This script generates a unified OpenAPI/Swagger documentation file from all microservices

set -e

echo "🏥 Healthcare Platform - Unified Swagger Generator"
echo "================================================="

# Configuration
API_GATEWAY_URL="http://localhost:8080"
SWAGGER_AGGREGATOR_URL="http://localhost:8090"
OUTPUT_DIR="./swagger-docs"
UNIFIED_FILE="$OUTPUT_DIR/healthcare-platform-api.json"

# Service configurations - macOS compatible
SERVICES=(
    "auth-service|http://localhost:8081"
    "profile-service|http://localhost:8082"
    "booking-service|http://localhost:8083"
    "products|http://localhost:8084"
    "notification-service|http://localhost:8085"
)

# Create output directory
mkdir -p "$OUTPUT_DIR"
echo "📁 Created output directory: $OUTPUT_DIR"

# Function to check if service is running
check_service() {
    local service_name="$1"
    local service_url="$2"
    
    echo "🔍 Checking $service_name at $service_url..."
    
    if curl -s --connect-timeout 5 --max-time 10 "$service_url/actuator/health" > /dev/null 2>&1; then
        echo "✅ $service_name is running"
        return 0
    else
        echo "❌ $service_name is not running at $service_url"
        return 1
    fi
}

# Function to download swagger JSON from service
download_swagger() {
    local service_name="$1"
    local service_url="$2"
    local output_file="$OUTPUT_DIR/${service_name}-api.json"
    
    echo "📥 Downloading Swagger JSON from $service_name..."
    
    if curl -s --connect-timeout 10 --max-time 30 "$service_url/v3/api-docs" -o "$output_file"; then
        # Check if file is not empty and contains basic JSON structure
        if [ -s "$output_file" ] && grep -q '"openapi"' "$output_file" 2>/dev/null; then
            echo "✅ Downloaded: $output_file"
            return 0
        else
            echo "⚠️  Invalid or empty file downloaded from $service_name"
            # Show first few lines for debugging
            if [ -s "$output_file" ]; then
                echo "🔍 File content preview:"
                head -3 "$output_file" 2>/dev/null || echo "Cannot read file"
            fi
            rm -f "$output_file"
            return 1
        fi
    else
        echo "❌ Failed to download from $service_name"
        return 1
    fi
}

# Function to create comprehensive swagger file with all role-based APIs
create_basic_swagger() {
    echo "📝 Creating comprehensive API documentation with all role-based endpoints..."
    ./script/generate-complete-swagger.sh > /dev/null 2>&1 || {
        echo "⚠️  Falling back to basic swagger creation..."
        cat > "$UNIFIED_FILE" << 'EOF'
{
  "openapi": "3.0.3",
  "info": {
    "title": "Healthcare Platform API",
    "description": "Complete API Documentation for Healthcare Microservices Platform with Role-Based Access Control. Supports Customer, Provider, and Admin user roles with comprehensive healthcare e-commerce functionality.",
    "version": "v2.0.0",
    "contact": {
      "name": "Healthcare Platform Team",
      "email": "support@healthcare.com"
    }
  },
  "servers": [
    {"url": "http://localhost:8080", "description": "API Gateway - Production Access"},
    {"url": "http://localhost:8090", "description": "Swagger Aggregator"}
  ],
  "tags": [
    {"name": "Authentication", "description": "User authentication and authorization"},
    {"name": "Customer", "description": "Customer-specific operations"},
    {"name": "Provider", "description": "Healthcare provider operations"},
    {"name": "Admin", "description": "Administrative operations"}
  ],
  "paths": {
    "/v1/auth/register": {
      "post": {
        "tags": ["Authentication"],
        "summary": "Register new user",
        "description": "Register a new user with role-based access (CUSTOMER, PROVIDER, or ADMIN)",
        "requestBody": {
          "required": true,
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "required": ["email", "password", "confirmPassword", "role"],
                "properties": {
                  "email": {"type": "string", "format": "email"},
                  "password": {"type": "string", "minLength": 8},
                  "confirmPassword": {"type": "string", "minLength": 8},
                  "role": {"type": "string", "enum": ["CUSTOMER", "PROVIDER", "ADMIN"]}
                }
              }
            }
          }
        },
        "responses": {
          "201": {"description": "User registered successfully"},
          "400": {"description": "Invalid registration data"},
          "409": {"description": "User already exists"}
        }
      }
    },
    "/v1/auth/login": {
      "post": {
        "tags": ["Authentication"],
        "summary": "User login",
        "description": "Authenticate user and return JWT tokens with role information",
        "responses": {
          "200": {"description": "Login successful"},
          "401": {"description": "Invalid credentials"}
        }
      }
    },
    "/v1/customer/dashboard": {
      "get": {
        "tags": ["Customer"],
        "summary": "Get customer dashboard",
        "security": [{"bearerAuth": []}],
        "responses": {
          "200": {"description": "Dashboard retrieved successfully"},
          "403": {"description": "Customer access required"}
        }
      }
    },
    "/v1/provider/dashboard": {
      "get": {
        "tags": ["Provider"],
        "summary": "Get provider dashboard",
        "security": [{"bearerAuth": []}],
        "responses": {
          "200": {"description": "Dashboard retrieved successfully"},
          "403": {"description": "Provider access required"}
        }
      }
    },
    "/v1/admin/dashboard": {
      "get": {
        "tags": ["Admin"],
        "summary": "Get admin dashboard",
        "security": [{"bearerAuth": []}],
        "responses": {
          "200": {"description": "Dashboard retrieved successfully"},
          "403": {"description": "Admin access required"}
        }
      }
    },
    "/actuator/health": {
      "get": {
        "tags": ["Health"],
        "summary": "Service health check",
        "responses": {
          "200": {"description": "Service is healthy"}
        }
      }
    }
  },
  "components": {
    "securitySchemes": {
      "bearerAuth": {
        "type": "http",
        "scheme": "bearer",
        "bearerFormat": "JWT"
      }
    }
  }
}
EOF
    }
}

# Main execution
echo "🔍 Checking services..."

# First try Swagger Aggregator
if check_service "swagger-aggregator" "$SWAGGER_AGGREGATOR_URL"; then
    echo "🚀 Using Swagger Aggregator for unified documentation"
    
    if curl -s --connect-timeout 10 --max-time 30 "$SWAGGER_AGGREGATOR_URL/v3/api-docs" -o "$UNIFIED_FILE"; then
        if [ -s "$UNIFIED_FILE" ]; then
            echo "✅ Unified documentation downloaded: $UNIFIED_FILE"
            
            # Try to download individual service docs through aggregator
            for service in "auth-service" "profile-service" "booking-service" "products" "notification-service"; do
                service_file="$OUTPUT_DIR/${service}-api.json"
                if curl -s --connect-timeout 10 "$SWAGGER_AGGREGATOR_URL/v3/api-docs/$service" -o "$service_file" 2>/dev/null; then
                    if [ -s "$service_file" ]; then
                        echo "✅ Downloaded service: $service_file"
                    else
                        rm -f "$service_file"
                    fi
                fi
            done
        else
            echo "⚠️  Swagger aggregator returned empty response, trying individual services..."
        fi
    else
        echo "❌ Failed to download from swagger aggregator, trying individual services..."
    fi
fi

# If aggregator failed or file is empty, try API Gateway
if [ ! -s "$UNIFIED_FILE" ]; then
    if check_service "api-gateway" "$API_GATEWAY_URL"; then
        echo "🚀 API Gateway is available - creating basic info file"
        create_basic_swagger
        echo "✅ Basic API info created: $UNIFIED_FILE"
    fi
fi

# Download from individual services
echo "📥 Downloading from individual services..."
download_count=0

for service_config in "${SERVICES[@]}"; do
    IFS='|' read -r service_name service_url <<< "$service_config"
    
    if check_service "$service_name" "$service_url"; then
        if download_swagger "$service_name" "$service_url"; then
            ((download_count++))
        fi
    fi
done

# If no unified file exists yet, create basic one
if [ ! -s "$UNIFIED_FILE" ]; then
    echo "📝 Creating basic unified swagger file..."
    create_basic_swagger
fi

# Create index file
cat > "$OUTPUT_DIR/README.md" << 'EOF'
# Healthcare Platform API Documentation

This directory contains OpenAPI/Swagger documentation for the Healthcare Platform microservices.

## Files Generated

- `healthcare-platform-api.json` - Unified API documentation
- `*-api.json` - Individual service API documentation

## Usage

### For Angular Code Generation

Use the unified file for complete API client generation:

```bash
npx @openapitools/openapi-generator-cli generate \
  -i ./swagger-docs/healthcare-platform-api.json \
  -g typescript-angular \
  -o ./src/app/api-client \
  --additional-properties=npmName=healthcare-api-client
```

### Individual Services

You can also use individual service documentation files for specific integrations.

## API Endpoints

- **Swagger Aggregator**: http://localhost:8090
- **API Gateway**: http://localhost:8080
- **Auth Service**: http://localhost:8081
- **Profile Service**: http://localhost:8082
- **Booking Service**: http://localhost:8083
- **Products Service**: http://localhost:8084
- **Notification Service**: http://localhost:8085

## Swagger UI Access

Once services are running, access Swagger UI at:
- Unified UI: http://localhost:8090/swagger-ui.html
- Individual services: http://localhost:<port>/swagger-ui.html

EOF

echo ""
echo "📋 Summary:"
echo "==========="
echo "📁 Output directory: $OUTPUT_DIR"
echo "📄 Main file: $UNIFIED_FILE"
echo "📚 Documentation index: $OUTPUT_DIR/README.md"
echo "📊 Individual services downloaded: $download_count"
echo ""

if [ -s "$UNIFIED_FILE" ]; then
    echo "🎯 For Angular code generation, use:"
    echo "npx @openapitools/openapi-generator-cli generate \\"
    echo "  -i $UNIFIED_FILE \\"
    echo "  -g typescript-angular \\"
    echo "  -o ./src/app/api-client \\"
    echo "  --additional-properties=npmName=healthcare-api-client"
    echo ""
    echo "✨ Documentation generation completed successfully!"
else
    echo "⚠️  No unified documentation could be generated."
    echo "💡 Make sure at least one service is running and try again."
fi