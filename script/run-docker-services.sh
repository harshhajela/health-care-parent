#!/bin/bash

#To run MongoDb
docker run -d -p 27017:27017 --name mongo-db -v data-vol:/data/db mongo:latest

#To run maildev
docker run -d -p 1080:1080 -p 1025:1025 --name maildev maildev/maildev


#To run loki
docker run -d -p 3100:3100 --name loki grafana/loki:main -config.file=/etc/loki/local-config.yaml

#To run prometheus
docker run -d -p 9090:9090 --name prometheus -v $(pwd)/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml:ro \
    prom/prometheus:v2.46.0 --enable-feature=exemplar-storage --config.file=/etc/prometheus/prometheus.yml

#To run Tempo
docker run -d -p 3110:3100 -p 9411:9411 --name tempo \
    -v $(pwd)/tempo/tempo.yml:/etc/tempo.yaml:ro -v $(pwd)/tempo/tempo-data:/tmp/tempo \
    grafana/tempo:2.2.2 -config.file=/etc/tempo.yaml

#To run Grafana
docker run -d -p 3000:3000 --name grafana -v $(pwd)/grafana:/etc/grafana/provisioning/datasources:ro \
    -e GF_AUTH_ANONYMOUS_ENABLED=true -e GF_AUTH_ANONYMOUS_ORG_ROLE=Admin -e GF_AUTH_DISABLE_LOGIN_FORM=true \
    grafana/grafana:10.1.0

#To run Zookeeper
docker run -d --name zookeeper-container -p 2181:2181 wurstmeister/zookeeper


#To run Kafka
docker run -d --name kafka-container -p 9092:9092 \
  -e KAFKA_ADVERTISED_HOST_NAME=localhost -e KAFKA_ZOOKEEPER_CONNECT=host.docker.internal:2181 \
  wurstmeister/kafka
