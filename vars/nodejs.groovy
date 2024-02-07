def call(Map configMap) {
    pipeline {
        agent {
            node {
                label 'agent-1'
            }
        }
        environment{
            projectName = "roboshop"
            component = "catalogue"
            nexusURL = "172.31.35.84:8081"
            packageVersion = ''
        }
        options {
            disableConcurrentBuilds()
            ansiColor('xterm')
        }
        parameters {
            booleanParam(name: 'DEPLOY', defaultValue: false, description: 'Set to true if deployment ready')
        }
        // Build stage
        stages {
            stage('Get the version'){
                steps {
                    script {
                        def packageJSON = readJSON file: "package.json"
                        packageVersion = packageJSON.version
                        echo "Application verison: ${packageVersion}"
                    }
                }
            }
            stage('Install dependencies') {
                steps {
                    sh """
                        npm install
                    """
                }
            }
            stage('Unit tests') {
                steps {
                    sh """
                        echo "Unit tests will run here"
                    """
                }
            }
            stage("Sonar scan"){
                steps {
                    sh """
                        sonar-scanner
                    """
                }
            }
            stage('Build'){
                steps {
                    sh """
                        zip -q -r ${component}.zip ./* -x ".git" -x "*.zip" -x "Jenkinsfile"
                    """
                }
            }
            stage('Publish artifact to Nexus'){
                steps {
                    nexusArtifactUploader(
                        nexusVersion: 'nexus3',
                        protocol: 'http',
                        nexusUrl: "${nexusURL}",
                        groupId: "com.${projectName}",
                        version: "${packageVersion}",
                        repository: "${component}",
                        credentialsId: 'nexus-auth',
                        artifacts: [
                            [artifactId: "${component}",
                            classifier: '',
                            file: "${component}.zip",
                            type: 'zip']
                        ]
                    )
                }
            }
            stage('Trigger Deploy job'){
                when {
                    expression {
                        params.DEPLOY
                    }
                }
                steps {
                    script {
                        def myParams = [
                            string(name: 'version', value: "$packageVersion"),
                            string(name: 'environment', value: "dev")
                        ]
                        build job: "catalogue-deploy", wait: false, parameters: myParams
                    }
                }
            }
        }
        // post build
        post {
            always {
                deleteDir()
            }
            success {
                echo "Runs only if pipeline is succeded"
            }
            failure {
                echo "Runs only if pipeline is failed"
            }
            changed {
                echo "Runs only if there is change in state compared to previous"
            }
        }
    }
}