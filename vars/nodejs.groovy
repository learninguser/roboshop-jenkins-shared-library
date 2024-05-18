def call(){
    node {
        common.checkout()
        common.codeQuality()
        common.testCases("nodejs")
        common.release()
    }
}