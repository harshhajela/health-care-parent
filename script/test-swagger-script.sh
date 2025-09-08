#!/bin/bash

# Simple test script for swagger generation (macOS compatible)
set -e

echo "🏥 Testing Swagger Generation - macOS Compatible"
echo "==============================================="

# Configuration
OUTPUT_DIR="./swagger-docs"
UNIFIED_FILE="$OUTPUT_DIR/healthcare-platform-api.json"

# Create output directory
mkdir -p "$OUTPUT_DIR"
echo "📁 Created output directory: $OUTPUT_DIR"

# Test basic JSON creation
echo "📝 Creating test swagger file..."
cat > "$UNIFIED_FILE" << 'EOF'
{
  "openapi": "3.0.3",
  "info": {
    "title": "Healthcare Platform API",
    "description": "Complete API Documentation for Healthcare Microservices Platform",
    "version": "v1.0",
    "contact": {
      "name": "Healthcare Platform Team",
      "email": "support@healthcare.com"
    }
  },
  "servers": [
    {"url": "http://localhost:8080", "description": "API Gateway"},
    {"url": "http://localhost:8081", "description": "Auth Service"},
    {"url": "http://localhost:8082", "description": "Profile Service"},
    {"url": "http://localhost:8083", "description": "Booking Service"},
    {"url": "http://localhost:8084", "description": "Products Service"},
    {"url": "http://localhost:8085", "description": "Notification Service"}
  ],
  "paths": {
    "/auth/login": {
      "post": {
        "tags": ["Authentication"],
        "summary": "User login",
        "description": "Authenticate user and return JWT tokens",
        "requestBody": {
          "required": true,
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "properties": {
                  "email": {"type": "string", "format": "email"},
                  "password": {"type": "string"}
                },
                "required": ["email", "password"]
              }
            }
          }
        },
        "responses": {
          "200": {
            "description": "Login successful",
            "content": {
              "application/json": {
                "schema": {
                  "type": "object",
                  "properties": {
                    "accessToken": {"type": "string"},
                    "refreshToken": {"type": "string"}
                  }
                }
              }
            }
          },
          "401": {"description": "Invalid credentials"}
        }
      }
    },
    "/auth/register": {
      "post": {
        "tags": ["Authentication"],
        "summary": "User registration",
        "description": "Register a new user account",
        "responses": {
          "201": {"description": "User registered successfully"}
        }
      }
    },
    "/bookings": {
      "get": {
        "tags": ["Bookings"],
        "summary": "Get booking history",
        "responses": {
          "200": {"description": "Booking history retrieved"}
        }
      },
      "post": {
        "tags": ["Bookings"],
        "summary": "Create new booking",
        "responses": {
          "201": {"description": "Booking created"}
        }
      }
    },
    "/healthcare-services": {
      "get": {
        "tags": ["Healthcare Services"],
        "summary": "Get all healthcare services",
        "responses": {
          "200": {"description": "Services retrieved"}
        }
      }
    },
    "/profile": {
      "get": {
        "tags": ["Profile"],
        "summary": "Get user profile",
        "responses": {
          "200": {"description": "Profile retrieved"}
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
  },
  "security": [{"bearerAuth": []}]
}
EOF

# Check if file was created
if [ -f "$UNIFIED_FILE" ]; then
    echo "✅ Test swagger file created: $UNIFIED_FILE"
    echo "📊 File size: $(wc -c < "$UNIFIED_FILE") bytes"
    
    # Create README
    cat > "$OUTPUT_DIR/README.md" << 'EOF'
# Healthcare Platform API Documentation

## Test File Generated

This is a test/basic swagger file for the Healthcare Platform.

### For Angular Code Generation

```bash
npx @openapitools/openapi-generator-cli generate \
  -i ./swagger-docs/healthcare-platform-api.json \
  -g typescript-angular \
  -o ./src/app/api-client \
  --additional-properties=npmName=healthcare-api-client
```

### API Endpoints Covered

- Authentication (login, register)
- Booking management
- Healthcare services
- User profiles

EOF

    echo "📚 Created README.md"
    echo ""
    echo "🎯 For Angular code generation, use:"
    echo "npx @openapitools/openapi-generator-cli generate \\"
    echo "  -i $UNIFIED_FILE \\"
    echo "  -g typescript-angular \\"
    echo "  -o ./src/app/api-client \\"
    echo "  --additional-properties=npmName=healthcare-api-client"
    echo ""
    echo "✅ Test completed successfully!"
else
    echo "❌ Failed to create test file"
    exit 1
fi