#!/bin/bash

docker run -d \
    -p 27017:27017 \
    --name mongo-db \
    -v data-vol:/data/db \
    mongo:latest

docker run -d -p 1080:1080 -p 1025:1025 --name maildev maildev/maildev


docker run --detach --name activemq -p 61616:61616 -p 8161:8161 --rm apache/activemq-artemis:latest-alpine