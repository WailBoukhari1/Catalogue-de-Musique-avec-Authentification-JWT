// Helper functions
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

// Helper function to read and modify docker-compose.yml
def getDockerComposeTemplate() {
    def composeFile = readFile 'docker-compose.yml'
    def modifiedCompose = composeFile
        .replaceAll('\\$\\{SPRING_PROFILES_ACTIVE\\}', 'prod')
        .replaceAll('\\$\\{MONGODB_HOST\\}', 'mongodb')
        .replaceAll('\\$\\{MONGODB_PORT\\}', '27017')
        .replaceAll('\\$\\{APP_PORT\\}', "${APP_PORT}")
        .replaceAll('\\$\\{DOCKER_IMAGE\\}', "${DOCKERHUB_REPOSITORY}:latest")
        
    return modifiedCompose
}

// Pipeline
pipeline {
    agent any

    environment {
        // Docker configurations
        DOCKER_IMAGE = 'musical-catalog-api'
        DOCKER_TAG = "${BUILD_NUMBER}"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
        DOCKERHUB_REPOSITORY = 'tsukiiya101/musical-catalog-api'
        DOCKERHUB_MONGODB_REPOSITORY = 'tsukiiya101/mongodb'
        DOCKERHUB_SONARQUBE_REPOSITORY = 'tsukiiya101/sonarqube'
        
        // Application configurations
        APP_PORT = '8080'
        PROJECT_PATH = 'C:\\Users\\ssngn\\Documents\\Youcode\\Catalogue-de-Musique-avec-Authentification-JWT'
    }

    tools {
        maven 'Maven 3.9.6'
        jdk 'JDK 17'
    }

    stages {
        stage('Initialize') {
            steps {
                script {
                    // Clean workspace and copy project files
                    cleanWs()
                    bat """
                        echo "Copying project files..."
                        xcopy /E /I /Y "${PROJECT_PATH}\\*" .
                    """
                }
            }
        }

        stage('Build & Test') {
            stages {
                stage('Build') {
                    steps {
                        bat "mvn clean package -DskipTests"
                    }
                }

                stage('Unit Tests') {
                    steps {
                        bat "mvn test"
                    }
                    post {
                        always {
                            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                        }
                    }
                }
            }
        }

        stage('Docker Build & Push') {
            stages {
                stage('Build Images') {
                    steps {
                        script {
                            bat """
                                echo "Building Docker images..."
                                
                                REM Build main application image
                                docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                                docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKERHUB_REPOSITORY}:${DOCKER_TAG}
                                docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKERHUB_REPOSITORY}:latest
                                
                                REM Tag MongoDB image
                                docker pull mongo:latest
                                docker tag mongo:latest ${DOCKERHUB_MONGODB_REPOSITORY}:latest
                                
                                REM Tag SonarQube image
                                docker pull sonarqube:latest
                                docker tag sonarqube:latest ${DOCKERHUB_SONARQUBE_REPOSITORY}:latest
                            """
                        }
                    }
                }

                stage('Push Images') {
                    steps {
                        script {
                            withCredentials([usernamePassword(
                                credentialsId: 'dockerhub-credentials',
                                usernameVariable: 'DOCKER_USERNAME',
                                passwordVariable: 'DOCKER_PASSWORD'
                            )]) {
                                bat """
                                    @echo off
                                    echo "Logging in to Docker Hub..."
                                    echo %DOCKER_PASSWORD% | docker login -u %DOCKER_USERNAME% --password-stdin
                                    
                                    echo "Pushing images to Docker Hub..."
                                    docker push --quiet ${DOCKERHUB_REPOSITORY}:${DOCKER_TAG}
                                    docker push --quiet ${DOCKERHUB_REPOSITORY}:latest
                                    docker push --quiet ${DOCKERHUB_MONGODB_REPOSITORY}:latest
                                    docker push --quiet ${DOCKERHUB_SONARQUBE_REPOSITORY}:latest
                                    
                                    echo "Logging out from Docker Hub..."
                                    docker logout
                                """
                            }
                        }
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Create production docker-compose file
                    writeFile file: 'docker-compose.prod.yml', text: getDockerComposeTemplate()

                    bat """
                        echo "Starting Production Deployment..."
                        
                        echo "Stopping existing containers (keeping images)..."
                        docker stop musical-catalog-api mongodb || true
                        docker rm musical-catalog-api mongodb || true
                        
                        echo "Cleaning up networks..."
                        docker network rm musical-catalog_app-network || true
                        
                        echo "Starting services..."
                        docker-compose -f docker-compose.prod.yml up -d --force-recreate
                        
                        echo "Waiting for services to be ready..."
                        ping -n 30 127.0.0.1 >nul
                        
                        echo "Verifying deployment..."
                        docker ps | findstr "mongodb"
                        docker ps | findstr "${DOCKER_IMAGE}"
                        
                        echo "Checking application logs..."
                        docker logs --tail 50 ${DOCKER_IMAGE}
                    """
                }
            }
        }
    }

    post {
        always {
            script {
                bat """
                    echo "Minimal cleanup..."
                    if exist docker-compose.prod.yml (
                        del docker-compose.prod.yml
                    )
                """
                cleanWs()
            }
        }
        success {
            echo 'Pipeline completed successfully! Containers are running locally.'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
} 