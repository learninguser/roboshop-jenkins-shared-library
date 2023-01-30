def compile(){
    if(app_lang == "nodejs"){
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

def unittests(){
    if(app_lang == "nodejs"){
        // There are no test cases written by the developer
        // println("Test cases");
        try{
            sh "npm test" 
        } catch(exception e){
            send_email("Unit test cases failed")
        }
    }
    if(app_lang == "maven"){
        sh "mvn test"
    }
    if(app_lang == "python"){
        sh "python -m unittest"
    }

}

def send_email(message){
    println(message);
}