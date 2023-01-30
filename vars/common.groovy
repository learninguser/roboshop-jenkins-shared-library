def compile(){
    if(app_lang == "nodejs"){
        // sh 'env'
        sh "npm install"
    }
    if(app_lang == "maven"){
        sh "mvn package"
    }
    if(app_lang == "golang"){
        sh "go mod init $component"
        sh "go get"
        sh "go build"
    }
}

def unittest(){
    if(app_lang == "nodejs"){
        // There are no test cases written by the developer
        // println("Test cases");
        sh "npm test"
    }
    if(app_lang == "maven"){
        sh "mvn test"
    }
    if(app_lang == "python"){
        sh "python -m unittest"
    }

}

def send_email(message){
    // println(message);
    mail bcc: '', body: "Job Failed - ${JOB_BASE_NAME}\nJenkins URL - ${JOB_URL}", cc: '', from: 'icspavan@gmail.com', replyTo: '', subject: "Jenkins Job Failed - ${JOB_BASE_NAME}", to: 'icspavan@gmail.com'
}