#!/bin/bash

# For customer-service
echo "customer-service build started..!!"
docker stop customer-service
docker rm customer-service
docker rmi customer-service-img
echo "customer-service image deleted!"
cd ./customer-service

IMAGE_NAME="customer-service-img"
# Clean and build
mvn clean package -DskipTests=true
#mvn clean package -DskipTests=true spring-boot:build-image
echo "customer-service clean and packaged!"
# Build the Docker image
docker build -t "$IMAGE_NAME" .

echo "Docker image '$IMAGE_NAME' built successfully!"
