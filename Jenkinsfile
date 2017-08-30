pipeline {
    agent any
    options {
        disableConcurrentBuilds()
        timestamps()
        timeout(time: 1, unit: 'HOURS')
    }
    parameters {
        choice(name: 'STAGE', description: '', choices: 'true\nfalse')
    }
    environment {
        MAVEN = "3.3.9"
        HOME = "/tmp/"
    }
    stages {
        stage('NPM install') {
            steps {
                sh 'docker pull maven:${MAVEN}'
                withDockerContainer(image: "maven:${MAVEN}", toolName: 'latest') {
                    sh "mvn clean compile test -Dstage=${STAGE}"
                }
            }
        }
    }
}
