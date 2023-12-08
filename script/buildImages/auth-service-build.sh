#!/bin/bash

# For auth-service
echo "auth-service build started..!!"
docker stop auth-service
docker rm auth-service
docker rmi auth-service-img
echo "auth-service image deleted!"
cd ./auth-service

IMAGE_NAME="auth-service-img"
# Clean and build
mvn clean package -DskipTests=true
echo "auth-service clean and packaged!"
# Build the Docker image
docker build -t "$IMAGE_NAME" .

echo "Docker image '$IMAGE_NAME' built successfully!"
