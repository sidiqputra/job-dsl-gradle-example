node('master') {
    def rootDir = pwd()
#    def example = load "${rootDir}@script/src/jobs/hello-job/development/hello.groovy"
    systemGroovyCommand("${rootDir}@script/src/jobs/hello-job/development/hello.groovy")
}
