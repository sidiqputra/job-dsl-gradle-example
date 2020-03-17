import groovy.json.JsonSlurper

def inputFile = readFileFromWorkspace("./src/jobs/test-job/deployment-backend.json")
def InputJSON = new JsonSlurper().parseText(inputFile)

for(i=0; i<InputJSON.squads.size(); i++ ) {
  def squad = InputJSON.squads[i]
  def host_target = squad.host
  def worker = squad.worker

  for (j = 0; j < squad.project.size(); j++) {
    def project_name = squad.project[j].name

    pipelineJob("${project_name}") {

      properties {
        disableConcurrentBuilds()
      }

      definition {
        cps {
          sandbox()
          script('''
node("''' + worker + '''") {
    def server = Artifactory.server 'art-1'
    def buildInfo
    def rtMaven
    try {
        stage ('Prepare code') {
            git([url: 'git@github.com:sidiqputra/''' + project_name + '''.git', branch: 'develop'])
            notifyBuild('STARTED')
        }

        stage ('Artifactory configuration') {

        }

        stage ('Install') {
        }

        stage ('Deploy') {

        }
  } catch (e) {
    currentBuild.result = "FAILED"
    throw e
  } finally {
    notifyBuild(currentBuild.result)
  }
}

def notifyBuild(String buildStatus = 'STARTED') {
  wrap([$class: 'BuildUser']) {

  def user = "${BUILD_USER}"
  def subject = ""
  def summary = ""
  def colorName = 'RED'
  def colorCode = '#FF0000'
  def details = """<p>STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
    <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>"""

  author = sh(returnStdout: true, script: "git --no-pager show -s --format='%an' `git rev-parse HEAD`").trim()
  buildStatus =  buildStatus ?: 'SUCCESS'


  if (user == 'SCMTrigger' ){
      user = "${author}"
  }

  if (buildStatus == 'STARTED') {
    subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' Started by ${user}"
    summary = "${subject} (${env.BUILD_URL})"
  } else {
    subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
    summary = "${subject} (${env.BUILD_URL})"
  }

  if (buildStatus == 'STARTED') {
    color = 'YELLOW'
    colorCode = '#FFFF00'
  } else if (buildStatus == 'SUCCESS') {
    color = 'GREEN'
    colorCode = '#00FF00'
  } else {
    color = 'RED'
    colorCode = '#FF0000'
  }

  slackSend (color: colorCode, message: summary)
  }
}
          '''.stripIndent())
        }
      }
    }
  }
}
