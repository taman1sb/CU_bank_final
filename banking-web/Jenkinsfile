pipeline {
    agent any

    stages {
        stage ('Compile Stage') {

            steps {
                withMaven(maven : 'maven_3_6_3') {
                    bat 'mvn clean compile'
                }
            }
        }

        stage ('Testing Stage') {

            steps {
                withMaven(maven : 'maven_3_6_3') {
                    bat 'mvn test'
                }
            }
        }


        stage ('Packing Stage') {
            steps {
                withMaven(maven : 'maven_3_6_3') {
                    bat 'mvn package'
                }
            }
        }
        
                
        stage ('Deployment Stage') {
            steps {
                withMaven(maven : 'maven_3_6_3') {
                    bat 'mvn tomcat7:redeploy'
                }
            }
        }
    }
 }   