pipeline {
    agent any
    options {
        disableConcurrentBuilds()
        timestamps()
        timeout(time: 1, unit: 'HOURS')
    }
    parameters {
        choice(name: 'ENV', description: '', choices: 'stage\nproduction\ncd2')
        choice(name: 'SUITE', description: '', choices: 'reels.xml\nsmoke.xml\nlimits.xml')
    }
    environment {
        MAVEN = "3.3.9"
        HOME = "/tmp/"
    }
    stages {
        stage('Execute tests') {
            steps {
                sh 'docker pull maven:${MAVEN}'
                withDockerContainer(image: "maven:${MAVEN}", toolName: 'latest') {
                    sh "mvn install -Denv=${env.ENV} -DsuiteXmlFile=${env.SUITE}"
                }
            }
        }
    }
}
