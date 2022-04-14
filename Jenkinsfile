pipeline {
    agent any
    environment {
        DOCKER_PASSWORD = credentials("docker_password")
        GITHUB_TOKEN = credentials("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")
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
                    MAJOR_VERSION = sh([script: 'git tag | cut -d . -f 1', returnStdout: true]).trim()
                    MINOR_VERSION = sh([script: 'git tag | cut -d . -f 2', returnStdout: true]).trim()
                    PATCH_VERSION = sh([script: 'git tag | cut -d . -f 3', returnStdout: true]).trim()
                }
                sh "docker build -t sorinnsg/hello-img:${MAJOR_VERSION}.\$((${MINOR_VERSION} +1)).${PATCH_VERSION} ."
            }
        }
    }
}
