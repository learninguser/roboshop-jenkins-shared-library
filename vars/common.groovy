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
        sh "sonar-scanner -Dsonar.qualitygate.wait=true -Dsonar.token=sqa_fe67dd14b0a22f7437ea212c77ba7e23feb9b356 -Dsonar.host.url=http://172.31.13.135:9000 -Dsonar.projectKey=${env.COMPONENT}"
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