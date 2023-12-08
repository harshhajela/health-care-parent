#!/bin/bash

# For customer-service
echo "user-service build started..!!"
docker stop user-service
docker rm user-service
docker rmi user-service-img
echo "user-service image deleted!"
cd ./user-service

IMAGE_NAME="user-service-img"
# Clean and build
mvn clean package -DskipTests=true

echo "user-service clean and packaged!"
# Build the Docker image
docker build -t "$IMAGE_NAME" .

echo "Docker image '$IMAGE_NAME' built successfully!"
