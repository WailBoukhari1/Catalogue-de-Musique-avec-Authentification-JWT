#!/bin/bash

# Create docker network if it doesn't exist
docker network create app-network || true

# Start Jenkins and SonarQube
docker-compose up -d jenkins sonarqube

# Wait for Jenkins to start
echo "Waiting for Jenkins to start..."
sleep 30

# Get the initial admin password
JENKINS_PASSWORD=$(docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword)
echo "Jenkins initial admin password: $JENKINS_PASSWORD"

# Install necessary Jenkins plugins
docker exec jenkins jenkins-plugin-cli --plugins \
    git \
    workflow-aggregator \
    docker-workflow \
    maven-plugin \
    junit \
    sonar \
    jacoco \
    docker-compose-build-step

echo "Jenkins is running at http://localhost:8082"
echo "SonarQube is running at http://localhost:9000" 