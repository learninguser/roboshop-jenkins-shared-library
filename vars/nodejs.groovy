def call(){
    node {
        try {
            common.checkout()
            common.codeQuality()
            common.testCases("nodejs")
            common.release()
        } catch (e) {
            common.notifyDeveloper()
        }
    }
}