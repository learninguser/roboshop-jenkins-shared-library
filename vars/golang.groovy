def call(){
    node {
        try {
            common.checkout()
            common.compile('golang')
            common.codeQuality()
            common.testCases("golang")
            common.release()
        } catch (e) {
            common.notifyDeveloper()
        }
    }
}