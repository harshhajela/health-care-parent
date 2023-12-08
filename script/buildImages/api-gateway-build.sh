#!/bin/bash

# For api-gateway
echo "api-gateway build started..!!"
docker stop api-gateway
docker rm api-gateway
docker rmi api-gateway-img
echo "api-gateway image deleted!"
cd ./api-gateway

IMAGE_NAME="api-gateway-img"
# Clean and build
mvn clean package -DskipTests=true
echo "api-gateway clean and packaged!"
# Build the Docker image
docker build -t "$IMAGE_NAME" .

echo "Docker image '$IMAGE_NAME' built successfully!"
