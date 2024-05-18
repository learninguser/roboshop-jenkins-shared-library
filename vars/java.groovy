def call(){
    node {
        common.checkout()
        common.compile('java')
        common.codeQuality()
        common.testCases("java")
        common.release()
    }
}