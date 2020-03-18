node('master') {
  stage('Checkout SCM') { 
    checkout scm
  }

  stage('Create a Project') {
    jobDsl scriptText: 'job("example-2")'

    jobDsl targets: ['src/jobs/hello-job/**/*.groovy', './src/jobs/test-job/**/*.groovy'].join('\n'),
           removedJobAction: 'DELETE',
           removedViewAction: 'DELETE',
           lookupStrategy: 'SEED_JOB'
  }
}
