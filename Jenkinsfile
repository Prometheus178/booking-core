pipeline {
    agent {
        node {
            label 'docker-agent'
            }
      }
       parameters {
          gitParameter branchFilter: 'origin/(.*)', defaultValue: 'master', name: 'BRANCH', type: 'PT_BRANCH'
       }
       stages {
          stage('Source') {
            steps {
              git branch: "${params.BRANCH}", url: 'https://github.com/jenkinsci/git-parameter-plugin.git'
            }
          }
       }
}