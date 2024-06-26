def checkout(){
    stage('Checkout code'){
        cleanWs() // clean the workspace
        git branch: 'master', url: "${env.REPO_URL}"
    }
}

def compile(appType){
    stage('Compile Code'){
        if (appType == 'java'){
            sh 'mvn clean compile'
        }
        if (appType == 'golang'){
            sh 'go mod init'
        }
    }
}

def codeQuality(){
    stage("Code Quality") {
        // values for sonar.host.url and sonar.login properties should be configured inside /opt/sonar/conf/sonar-scanner.properties file in the sonar server
        sh "sonar-scanner"
    }
}

def testCases(appType){
    stage("Unit tests"){
        if (appType == "java"){
            sh "mvn test || true"
        }
        if (appType == "nodejs"){
            sh "npm test || true"
        }
        if (appType == "python"){
            sh "python3 -m unittest *.py || true"
        }
        if (appType == "golang"){
            sh "go test *.go || true"
        }
    }
}

def release(){
    stage("Release a Package") {
        echo "Release a Package"
    }
}

def notifyDeveloper(){
    stage("Notify Developer"){
        mail bcc: '', body: "<h1>Pipeline Failure</h1><br>Project Name: ${COMPONENT}\nURL = ${BUILD_URL}", cc: '', charset: 'UTF-8', from: 'icspavan@gmail.com', mimeType: 'text/html', replyTo: 'icspavan@gmail.com', subject: "ERROR CI: Component Name - ${COMPONENT}", to: "icspavan@gmail.com"
        sh 'exit 1' // to mark pipeline job is a failure in jenkins UI
    }
}