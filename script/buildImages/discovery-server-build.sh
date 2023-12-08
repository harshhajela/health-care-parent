#!/bin/bash

# For customer-service
echo "discovery-server build started..!!"
docker stop discovery-server
docker rm discovery-server
docker rmi discovery-server-img
echo "discovery-server image deleted!"
cd ./discovery-server

IMAGE_NAME="discovery-server-img"
# Clean and build
mvn clean package -DskipTests=true
echo "discovery-server clean and packaged!"
# Build the Docker image
docker build -t "$IMAGE_NAME" .

echo "Docker image '$IMAGE_NAME' built successfully!"
