pipeline {
    agent any

    environment {
        IMAGE_NAME = "mazen375/spring-boot-api" // Docker image name
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the commit that triggered the webhook
                checkout scm
            }
        }

        stage('Run Controller Tests') {
            steps {
                echo "Running controller unit tests..."
                sh './mvnw clean test -Dtest=**/*ControllerTests.java'
            }
        }

        stage('Package App') {
            steps {
                echo "Packaging application..."
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Docker Build & Push') {
            steps {
                // Use Jenkins credentials safely
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub',       // Jenkins credential ID
                    usernameVariable: 'DOCKER_USER', // Temporary env variable
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
