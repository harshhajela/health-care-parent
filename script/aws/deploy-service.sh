#!/bin/bash

EC2_IP="13.40.134.124"
SSH_KEY="t2-medium-helping-hands.pem"

# Check if the script is provided with an argument
if [ "$#" -eq 0 ]; then
    echo "Deploy service script needs an argument. Exiting!"
    exit 1
fi

argument="$1"

# Check if the argument is "auth-service"
if [ "$argument" == "auth-service" ]; then
    echo "The provided argument is: "$argument
    cd ../../auth-service
    mvn clean install
    aws s3 cp target/*.jar s3://helping-hand-app/JARS/
    echo "Uploaded to AWS S3, starting deployment!"
    cd ../script/aws

    ssh -i "$(pwd)/t2-medium-helping-hands.pem" ec2-user@"$EC2_IP" << EOF
        # Run commands on the EC2 instance
        cd jars
        pkill -f "java -jar auth-service-1.0-SNAPSHOT.jar"
        rm -rf auth-service*
        wget https://helping-hand-app.s3.eu-west-2.amazonaws.com/JARS/auth-service-1.0-SNAPSHOT.jar
        nohup java -jar auth-service-1.0-SNAPSHOT.jar > auth-service.log 2>&1 &
EOF
    echo "Deployment of $argument is completed"
    exit 1
fi
