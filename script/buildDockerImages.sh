#!/bin/bash

#Build commons module
cd commons
mvn clean install
cd ..
echo 'Commons module built successfully!'

# For all services
echo "discovery-server image building"
./buildImages/discovery-server-build.sh

echo "api-gateway image building"
./buildImages/api-gateway-build.sh

echo "auth-service image building"
./buildImages/auth-service-build.sh

echo "profile-service image building"
./buildImages/profile-service-build.sh

echo "booking-service image building"
./buildImages/booking-service-build.sh

echo "notification-service image building"
./buildImages/notification-service-build.sh