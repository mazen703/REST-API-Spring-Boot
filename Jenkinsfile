pipeline {
    agent any

    environment {
        IMAGE_NAME = "mazen375/spring-boot-api" // Docker image name
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Clean') {
            steps {
                sh './mvnw clean'
            }
        }

        stage('Run Controller Tests') {
            steps {
                echo "Running controller unit tests..."
                sh './mvnw test -Dtest=*ControllerTests -DfailIfNoTests=false'
            }
        }

        stage('Package App') {
            steps {
                echo "Packaging application..."
                sh './mvnw package -DskipTests'
            }
        }

        stage('Docker Build & Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                    docker build -t ${IMAGE_NAME}:latest .
                    echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin
                    docker push ${IMAGE_NAME}:latest
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline succeeded! Docker image pushed.'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}

