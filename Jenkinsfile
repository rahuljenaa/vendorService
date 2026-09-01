pipeline {
    agent any

    environment {
        GIT_URL        = 'https://github.com/rahuljenaa/vendorService.git'
        GIT_BRANCH     = 'develop'
        GIT_CRED_ID    = 'github-cred'

        TOMCAT_URL     = 'http://localhost:8081'
        TOMCAT_CONTEXT = '/vendor'
        TOMCAT_CRED_ID = 'tomcat-cred'

        WAR_FILE       = 'target/vendor.war'

        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_CRED_ID  = 'sonar-token'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: "${GIT_BRANCH}",
                    credentialsId: "${GIT_CRED_ID}",
                    url: "${GIT_URL}"
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit allowEmptyResults: true,
                          testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: "${SONAR_CRED_ID}", variable: 'SONAR_TOKEN')]) {
                    sh '''
                    mvn org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                    -Dsonar.host.url=${SONAR_HOST_URL} \
                    -Dsonar.token=${SONAR_TOKEN} \
                    -Dsonar.login=${SONAR_TOKEN} \
                    -Dsonar.projectKey=vendorservice \
                    -Dsonar.projectName=vendorservice
                    '''
                }
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: "${TOMCAT_CRED_ID}",
                    usernameVariable: 'TC_USER',
                    passwordVariable: 'TC_PASS'
                )]) {

                    sh '''
                    curl -v -u "${TC_USER}:${TC_PASS}" \
                    -T "${WAR_FILE}" \
                    "${TOMCAT_URL}/manager/text/deploy?path=${TOMCAT_CONTEXT}&update=true"
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "Application deployed successfully."
            echo "URL: ${TOMCAT_URL}${TOMCAT_CONTEXT}"
        }

        failure {
            echo "Build or deployment failed. Check console logs."
        }
    }
}
