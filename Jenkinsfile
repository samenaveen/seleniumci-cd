pipeline {
    agent any
    
    triggers {
        // Trigger builds periodically (every 1 hour)
        // H H * * * means once a day at midnight
        pollSCM('H * * * *')
    }
    
    options {
        // Keep last 10 builds
        buildDiscarder(logRotator(numToKeepStr: '10'))
        // Timeout after 30 minutes
        timeout(time: 30, unit: 'MINUTES')
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code from repository...'
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building the project...'
                sh 'mvn clean compile -DskipTests'
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running test cases...'
                sh 'mvn test -Dsuite=login'
            }
        }
    }
    
    post {
        always {
            echo 'Collecting test results...'
            // Archive TestNG reports
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
            
            // Archive Extent Reports
            publishHTML([
                reportDir: 'reports',
                reportFiles: '*.html',
                reportName: 'Extent Report'
            ])
            
            // Archive screenshots
            publishHTML([
                reportDir: 'reports/screenshots',
                reportFiles: 'index.html',
                reportName: 'Screenshots'
            ])
            
            // Archive logs
            archiveArtifacts artifacts: 'logs/**/*.log', allowEmptyArchive: true
        }
        
        success {
            echo 'Tests executed successfully!'
        }
        
        failure {
            echo 'Test execution failed! Check the reports for details.'
        }
    }
}
