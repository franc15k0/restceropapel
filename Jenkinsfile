pipeline {
    agent any
    stages {
        stage('UpJboss') {
            steps {
                //sh './pipeline/jboss.sh'
                echo "Jbosss"
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Deploy') {
            steps {
               sh './pipeline/deploy.sh'
             // echo "Deploy"
            }
        }
    }
}