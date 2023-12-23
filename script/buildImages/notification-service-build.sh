#!/bin/bash

# For care-provider
echo "notification-service build started..!!"
docker stop notification-service
docker rm notification-service
docker rmi notification-service-img
echo "notification-service image deleted!"
cd ./notification-service

IMAGE_NAME="notification-service-img"
# Clean and build
mvn clean package -DskipTests=true
echo "notification-service clean and packaged!"
# Build the Docker image
docker build -t "$IMAGE_NAME" .

echo "Docker image '$IMAGE_NAME' built successfully!"
