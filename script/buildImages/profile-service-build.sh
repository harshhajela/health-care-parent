#!/bin/bash

# For care-provider
echo "profile-service build started..!!"
docker stop profile-service
docker rm profile-service
docker rmi profile-service-img
echo "profile-service image deleted!"
cd ./profile-service

IMAGE_NAME="profile-service-img"
# Clean and build
mvn clean package -DskipTests=true
echo "profile-service clean and packaged!"
# Build the Docker image
docker build -t "$IMAGE_NAME" .

echo "Docker image '$IMAGE_NAME' built successfully!"
