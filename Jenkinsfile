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

                sh "docker build -t sorinnsg/hello-img:${MAJOR_VERSION}.\$((${MINOR_VERSION} +1)).${PATCH_VERSION} ."

                withCredentials([string(credentialsId: 'Docker123', variable: 'DOCKER_PASSWORD')]) {
                sh '''
                set +x
                docker login docker.io -u sorinnsg -p ${env.DOCKER_PASSWORD}
                docker push sorinnsg/hello-img:${MAJOR_VERSION}.\$((${MINOR_VERSION} +1)).${PATCH_VERSION}
                '''
                }

                sh "git tag ${env.IMAGE_TAG}"
                withCredentials([string(credentialsId: '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', variable: 'GITHUB_TOKEN')]){
                sh "git push https://${GITHUB_TOKEN}@github.com/StockZ-ProdEngineering/service.git ${env.IMAGE_TAG}"
                }
            }
        }
        stage('Deploy'){
            steps{
                sh "IMAGE_TAG=${env.IMAGE_TAG} docker-compose up -d hello"
            }
        }
    }
}
