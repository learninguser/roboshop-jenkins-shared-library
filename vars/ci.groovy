def call(){
    
    if(!env.SONAR_EXTRA_OPTS) {
        env.SONAR_EXTRA_OPTS = " "
    }

    try{
        pipeline{
            node('workstation'){
                stage('Checkout'){
                    cleanWs()
                    git branch: 'master', url: "https://github.com/learninguser/${component}"
                }
                stage('Compile/Build'){
                    common.compile()
                }
                stage('Unit Tests'){
                    common.unittest()
                }
                stage('Quality control'){
                    SONAR_USER = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.user  --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
                    SONAR_PASS = sh ( script: 'aws ssm get-parameters --region us-east-1 --names sonarqube.password  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
                    wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${SONAR_PASS}", var: 'SECRET']]]) {
                        sh "sonar-scanner -Dsonar.host.url=http://sonarqube.learninguser.online:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=${SONAR_PASS} -Dsonar.projectKey=${component} ${SONAR_EXTRA_OPTS}"
                    }
                }
                stage('Upload code to centralised place'){
                    echo 'Upload code to centralised place'
                }
            }
        }
    } catch(Exception e){
        common.send_email("Unit test cases failed")
    }
}