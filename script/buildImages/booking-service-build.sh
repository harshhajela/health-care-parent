#!/bin/bash

# For booking-service
echo "booking-service build started..!!"
docker stop booking-service
docker rm booking-service
docker rmi booking-service-img
echo "booking-service image deleted!"
cd ./booking-service

IMAGE_NAME="booking-service-img"
# Clean and build
mvn clean package -DskipTests=true
echo "booking-service clean and packaged!"
# Build the Docker image
docker build -t "$IMAGE_NAME" .

echo "Docker image '$IMAGE_NAME' built successfully!"
