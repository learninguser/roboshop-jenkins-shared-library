def call(){
    
    if(!env.SONAR_EXTRA_OPTS) {
        env.SONAR_EXTRA_OPTS = " "
    }

    try{
        pipeline{
            agent{
                label 'workstation'
            }
            stages{
                stage('Compile/Build'){
                    steps{
                        script{
                            common.compile()
                        }
                    }
                }
                stage('Unit Tests'){
                    steps{
                        script{
                            common.unittest()
                        }
                    }
                }
                stage('Quality control'){
                    environment{
                        // SONAR_PASS = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.password  --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
                        SONAR_USER = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.user  --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
                    }
                    steps{
                        script{
                            SONAR_PASS = sh ( script: 'aws ssm get-parameters --region us-east-1 --names sonarqube.password  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
                            wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${SONAR_PASS}", var: 'SECRET']]]) {
                                sh "sonar-scanner -Dsonar.host.url=http://sonarqube.learninguser.online:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=${SONAR_PASS} -Dsonar.projectKey=${component} ${SONAR_EXTRA_OPTS}"
                            }
                        }
                    }
                }
                stage('Upload code to centralised place'){
                    steps{
                        echo 'Upload code to centralised place'
                    }
                }
            }
        }
    } catch(Exception e){
        common.send_email("Unit test cases failed")
    }
}