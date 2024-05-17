def checkout(){
    stage('Checkout code'){
        cleanWs() // clean the workspace
        echo "Pull latest code"
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
        echo "Code Quality"
    }
}

def release(){
    stage("Release a Package") {
        echo "Release a Package"
    }
}