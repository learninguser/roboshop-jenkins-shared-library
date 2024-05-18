def call(){
    node {
        try {
            common.checkout()
            common.codeQuality()
            common.testCases("python")
            common.release()
        } catch (e) {
            common.notifyDeveloper()
        }
    }
}