#!/bin/bash

docker stop tempo grafana prometheus loki

docker rm tempo grafana prometheus loki