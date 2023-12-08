#!/bin/bash

#aws s3 cp ../discovery-server/target/*.jar s3://helping-hand-app
#aws s3 cp ../api-gateway/target/*.jar s3://helping-hand-app
aws s3 cp ../auth-service/target/*.jar s3://helping-hand-app/JARS/
#aws s3 cp ../care-provider/target/*.jar s3://helping-hand-app/JARS/

