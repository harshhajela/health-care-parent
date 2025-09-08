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

