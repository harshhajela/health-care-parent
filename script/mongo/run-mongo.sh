#!/bin/bash

docker run -d \
    -p 27017:27017 \
    --name mongo-db \
    -v data-vol:/data/db \
    mongo:latest