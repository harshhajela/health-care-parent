#!/bin/bash

# For all services
echo "discovery-server image building"
./script/buildImages/discovery-server-build.sh

echo "api-gateway image building"
./script/buildImages/api-gateway-build.sh

echo "auth-service image building"
./script/buildImages/auth-service-build.sh

echo "customer-service image building"
./script/buildImages/customer-service-build.sh

echo "care-provider image building"
./script/buildImages/care-provider-build.sh

echo "user-service image building"
./script/buildImages/user-service-build.sh

echo "booking-service image building"
./script/buildImages/booking-service-build.sh