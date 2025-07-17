pipeline {
    agent any

    environment {
        IMAGE_NAME = "my-springboot-app"
        DOCKER_REGISTRY = "local" // Optional: set to your registry if pushing
    }

    stages {
        stage('Clone') {
            steps {
                git url: 'git@github.com:hoangsadn/altech-assignment.git', branch: 'main'
            }
        }

        stage('Build with Gradle') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('Build Docker Image') {
            steps {
                 sh "docker build -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Start with Docker Compose') {
            steps {
                sh 'docker compose down || true' // stop any previous
                sh 'docker compose up -d --build'
            }
        }
    }

    post {
        always {
            echo 'Cleaning up...'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
