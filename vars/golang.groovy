def call(){
    node {
        common.checkout()
        common.compile('golang')
        common.codeQuality()
        common.release()
    }
}