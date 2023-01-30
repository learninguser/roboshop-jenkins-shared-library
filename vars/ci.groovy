def call(){
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
                steps{
                    echo 'Quality control'
                }
            }
            stage('Upload code to centralised place'){
                steps{
                    echo 'Upload code to centralised place'
                }
            }
        }
    }
}