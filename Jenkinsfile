node('master') {
    jobDsl scriptText: 'job("example-2")'

    jobDsl targets: ['src/jobs/hello-job/development/hello.groovy', './src/jobs/test-job/**/*.groovy'].join('\n'),
           removedJobAction: 'DELETE',
           removedViewAction: 'DELETE',
           lookupStrategy: 'SEED_JOB'
}
