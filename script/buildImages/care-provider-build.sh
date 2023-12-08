#!/bin/bash

# For care-provider
echo "care-provider build started..!!"
docker stop care-provider
docker rm care-provider
docker rmi care-provider-img
echo "customer-service image deleted!"
cd ./care-provider

IMAGE_NAME="care-provider-img"
# Clean and build
mvn clean package -DskipTests=true
echo "care-provider clean and packaged!"
# Build the Docker image
docker build -t "$IMAGE_NAME" .

echo "Docker image '$IMAGE_NAME' built successfully!"
