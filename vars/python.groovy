def call(){
    node {
        common.checkout()
        common.codeQuality()
        common.testCases("python")
        common.release()
    }
}