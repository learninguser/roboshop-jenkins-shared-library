def call(){
    node {
        try {
            common.checkout()
            common.compile('java')
            common.codeQuality()
            common.testCases("java")
            common.release()
        } catch (e) {
            common.notifyDeveloper()
        }
    }
}