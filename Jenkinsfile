def waitForService(String url, int timeoutSeconds) {
    bat """
        echo Waiting for service at ${url}...
        for /l %%x in (1, 1, ${timeoutSeconds}) do (
            curl -s -f ${url} >nul 2>&1
            if not errorlevel 1 exit 0
            ping -n 2 127.0.0.1 >nul
        )
        exit 1
    """
}

pipeline {
    agent any

    environment {
        // Docker configurations
        DOCKER_IMAGE = 'musical-catalog-api'
        DOCKER_TAG = "${BUILD_NUMBER}"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
        DOCKERHUB_REPOSITORY = 'tsukiiya101/musical-catalog-api'
        
        // MongoDB configurations
        MONGODB_HOST = 'mongodb'
        MONGODB_PORT = '27017'
        
        // Application configurations
        APP_PORT = '8080'
        
        // SonarQube configurations
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_PROJECT_KEY = 'musical-catalog-api'
    }

    tools {
        maven 'Maven 3.9.6'
        jdk 'JDK 17'
    }

    stages {
        stage('Start Dependencies') {
            steps {
                script {
                    // Start services
                    bat "docker-compose up -d mongodb sonarqube"
                    
                    // Wait for SonarQube to be ready
                    waitForService("http://localhost:9000", 60)
                    // Wait for MongoDB to be ready
                    waitForService("http://localhost:27017", 30)
                }
            }
        }

        stage('Copy Project') {
            steps {
                bat """
                    xcopy /E /I /Y "C:\\Users\\ssngn\\Documents\\Youcode\\Catalogue-de-Musique-avec-Authentification-JWT\\*" .
                """
            }
        }

        stage('Build') {
            steps {
                bat "mvn clean package -DskipTests"
            }
        }

        stage('Tests') {
            steps {
                bat "mvn test"
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    // Wait for SonarQube to be ready
                    bat """
                        echo Waiting for SonarQube to be ready...
                        ping -n 30 127.0.0.1 >nul
                        mvn sonar:sonar ^
                        -Dsonar.host.url=${SONAR_HOST_URL} ^
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} ^
                        -Dsonar.java.binaries=target/classes ^
                        -Dsonar.login=admin ^
                        -Dsonar.password=admin
                    """
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    bat "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                    bat "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKERHUB_REPOSITORY}:${DOCKER_TAG}"
                    bat "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKERHUB_REPOSITORY}:latest"
                }
            }
        }

        stage('Push to DockerHub') {
            steps {
                script {
                    // Login to DockerHub
                    bat "echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin"
                    
                    // Push images
                    bat "docker push ${DOCKERHUB_REPOSITORY}:${DOCKER_TAG}"
                    bat "docker push ${DOCKERHUB_REPOSITORY}:latest"
                }
            }
            post {
                always {
                    bat "docker logout"
                }
            }
        }

        stage('Deploy Development') {
            when {
                branch 'develop'
            }
            steps {
                script {
                    bat """
                        docker network create app-network || exit 0
                        docker stop ${DOCKER_IMAGE}-dev || exit 0
                        docker rm ${DOCKER_IMAGE}-dev || exit 0
                        
                        docker run -d ^
                            --name ${DOCKER_IMAGE}-dev ^
                            --network app-network ^
                            -p 8081:8080 ^
                            -e SPRING_PROFILES_ACTIVE=dev ^
                            -e SPRING_DATA_MONGODB_HOST=${MONGODB_HOST} ^
                            -e SPRING_DATA_MONGODB_PORT=${MONGODB_PORT} ^
                            ${DOCKER_IMAGE}:${DOCKER_TAG}
                    """
                }
            }
        }

        stage('Deploy Production') {
            when {
                branch 'main'
            }
            steps {
                script {
                    bat """
                        docker network create app-network || exit 0
                        docker stop ${DOCKER_IMAGE} || exit 0
                        docker rm ${DOCKER_IMAGE} || exit 0
                        
                        docker run -d ^
                            --name ${DOCKER_IMAGE} ^
                            --network app-network ^
                            -p ${APP_PORT}:8080 ^
                            -e SPRING_PROFILES_ACTIVE=prod ^
                            -e SPRING_DATA_MONGODB_HOST=${MONGODB_HOST} ^
                            -e SPRING_DATA_MONGODB_PORT=${MONGODB_PORT} ^
                            ${DOCKER_IMAGE}:${DOCKER_TAG}
                    """
                }
            }
        }
    }

    post {
        always {
            script {
                // Cleanup
                bat """
                    docker image prune -f
                    docker-compose down
                """
            }
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
} 