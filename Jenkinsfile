pipeline {
    agent any
    environment {
    GIT_TAG = sh([script: 'git fetch --tag && git tag', returnStdout: true]).trim()
    MAJOR_VERSION = sh([script: 'git tag | sort --version-sort | cut -d . -f 1', returnStdout: true]).trim()
    MINOR_VERSION = sh([script: 'git tag | sort --version-sort | cut -d . -f 2', returnStdout: true]).trim()
    PATCH_VERSION = sh([script: 'git tag | sort --version-sort | cut -d . -f 3', returnStdout: true]).trim()
    IMAGE_TAG = "${env.MAJOR_VERSION}.\$((${env.MINOR_VERSION} + 1)).${env.PATCH_VERSION}"

    }

    stages {
        stage('Build & Test') {
            steps {
                sh './gradlew clean build'
            }
        }
        stage('Tag image') {
            steps {
                sh "docker build -t sorinnsg/hello-img:v1.1.3 ."
                //sh "docker build -t sorinnsg/hello-img:${MAJOR_VERSION}.\$((${MINOR_VERSION} +1)).${PATCH_VERSION} ."


                withCredentials([string(credentialsId: 'docker_password', variable: 'DOCKER_PASSWORD')]) {
                sh '''
                docker login docker.io -u sorinnsg -p $DOCKER_PASSWORD
                docker push sorinnsg/hello-img:v1.1.3
                '''
                }
//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
                sh "git tag v1.1.3" //image tag
                withCredentials([string(credentialsId: 'GITHUB_TOKEN', variable: 'GITHUB_TOKEN')]){
                sh "git push https://$GITHUB_TOKEN@github.com/StockZ-ProdEngineering/service.git v1.1.3" // img tag
                }
            }
        }
        stage('Deploy'){
            steps{
                sh "IMAGE_TAG=v1.1.3 docker-compose up -d hello" // img tag
            }
        }
    }
}
