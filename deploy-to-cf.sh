#!/bin/bash

if [ -z "$1" ]
  then
    echo "No unique string provided String which will guarantee global uniqueness across region in bluemix"
    exit 1
fi


#################################################################################
# Configuration Data
#################################################################################


#This can be updated to use any string which will guarantee global uniqueness across your region (username, favorite cat, etc.)
UNIQUE_IDENTIFIER="${1}"

#The name of the user-provided-service we will create to connect to Service Discovery servers
SERVICE_DISCOVERY_UPS="eureka-service-discovery"

# The domain associated with your Bluemix region
DOMAIN="w3ibm.mybluemix.net"

#
#################################################################################
# Deployment Code
#################################################################################

# Determine which JAR file we should use (since we have both Gradle and Maven possibilities)
RUNNABLE_JAR="$(find ./target -name "*-SNAPSHOT.jar" | sed -n 1p)"

echo "RUNNABLE_JAR : ${RUNNABLE_JAR}"

# Create the route ahead of time to control access
COMPONENT="ipat-dashboard-service"
CURRENT_SPACE=$(bx target | grep "Space:" | awk '{print $2}')
SERVICE_ROUTE="${COMPONENT}-${UNIQUE_IDENTIFIER}"

echo "SERVICE_ROUTE : ${SERVICE_ROUTE}"

bx cf create-route ${CURRENT_SPACE} ${DOMAIN} --hostname ${SERVICE_ROUTE}

# Push application code
# Push ipat-dashboard-service application code, leveraging metadata from manifest.yml
bx cf push \
         ${COMPONENT}-${UNIQUE_IDENTIFIER} \
      -p ${RUNNABLE_JAR} \
      -d ${DOMAIN} \
      -n ${SERVICE_ROUTE}	 
RUN_RESULT=$?

bx cf set-env ${COMPONENT}-${UNIQUE_IDENTIFIER} SPRING_PROFILES_ACTIVE CLOUD

bx cf bind-service ${COMPONENT}-${UNIQUE_IDENTIFIER} ${SERVICE_DISCOVERY_UPS}

bx cf restage ${COMPONENT}-${UNIQUE_IDENTIFIER}

RUN_RESULT=$?

if [ ${RUN_RESULT} -ne 0 ]; then
    echo "ipat-dashboard-service failed to start successfully.  Check logs in the local project directory for more details."
    exit 1
fi

bx cf apps