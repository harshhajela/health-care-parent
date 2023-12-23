#!/bin/bash

# For all services
echo "discovery-server image building"
./script/buildImages/discovery-server-build.sh

echo "api-gateway image building"
./script/buildImages/api-gateway-build.sh

echo "auth-service image building"
./script/buildImages/auth-service-build.sh

echo "profile-service image building"
./script/buildImages/profile-service-build.sh

echo "booking-service image building"
./script/buildImages/booking-service-build.sh

echo "notification-service image building"
./script/buildImages/notification-service-build.sh