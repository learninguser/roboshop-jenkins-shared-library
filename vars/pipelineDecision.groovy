#!groovy

def decidePipeline(Map configMap){
    application = configMap.get("application")
    switch(application) {
        case 'nodejs':
            nodejs(configMap)
            break
        case 'java':
            java(configMap)
            break
        default:
            error "Application is not recognised"
    }
}