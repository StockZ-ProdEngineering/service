pipeline {
    agent any
    environment {
    }

    stages {
        stage('Build & Test') {
            steps {
                sh './gradlew clean build'
            }
        }
        stage('Tag image') {
            steps {
                script {
                    sh([script: 'git fetch --tag', returnStdout: true]).trim()
                    GIT_TAG = sh([script: 'git fetch --tag && git tag', returnStdout: true]).trim()
                    env.MAJOR_VERSION = sh([script: 'git tag | sort --version-sort | cut -d . -f 1', returnStdout: true]).trim()
                    env.MINOR_VERSION = sh([script: 'git tag | sort --version-sort | cut -d . -f 2', returnStdout: true]).trim()
                    env.PATCH_VERSION = sh([script: 'git tag | sort --version-sort | cut -d . -f 3', returnStdout: true]).trim()
                    env.IMAGE_TAG = "${env.MAJOR_VERSION}.\$((${env.MINOR_VERSION} + 1)).${env.PATCH_VERSION}"
                    env.DOCKER_PASSWORD = credentials("Docker123")
                    env.GITHUB_TOKEN = credentials("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")
                }

                sh "docker build -t sorinnsg/hello-img:${MAJOR_VERSION}.\$((${MINOR_VERSION} +1)).${PATCH_VERSION} ."
                sh ''' docker login docker.io -u sorinnsg -p ${DOCKER_PASSWORD}
                       docker push sorinnsg/hello-img:${MAJOR_VERSION}.\$((${MINOR_VERSION} +1)).${PATCH_VERSION}
                '''
                sh "git tag ${env.IMAGE_TAG}"
                sh "git push https://$GITHUB_TOKEN@github.com/StockZ-ProdEngineering/service"
            }
        }
        stage('Deploy'){
            steps{
                sh "IMAGE_TAG=${env.IMAGE_TAG} docker-compose up -d hello"
            }
        }
    }
}
