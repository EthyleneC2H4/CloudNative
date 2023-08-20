pipeline {
    agent none
    environment {
        REGISTRY = "harbor.edu.cn/nju05"
    }
    stages {
        // 克隆代码
        stage('Clone Code') {
            agent {
                label 'master'
            }
            steps {
                echo "1.Git Clone Code"
                git url: "https://gitee.com/cpllouis1127/prometheus-test-demo.git"
            }
        }

        // maven 打包
        stage('Maven Build') {
            agent {
                docker {
                    image 'maven:latest'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                echo "2.Maven Build Stage"
                sh 'mvn -B clean package -Dmaven.test.skip=true'
            }
        }

        // 构建镜像
        stage('Image Build') {
            agent {
                label 'master'
            }
            steps {
                echo "3.Image Build Stage"
                sh 'docker build -f Dockerfile --build-arg jar_name=target/demo1-0.0.1-SNAPSHOT.jar -t prometheus-test-demo:${BUILD_ID} . '
                sh 'docker tag prometheus-test-demo:${BUILD_ID} ${REGISTRY}/prometheus-test-demo:${BUILD_ID}'
            }
        }

        // 推送镜像
        stage('Push') {
            agent {
                label 'master'
            }
            steps {
                echo "4.Push Docker Image Stage"
                sh "docker login --username=nju05 harbor.edu.cn -p nju052023"
                sh "docker push ${REGISTRY}/prometheus-test-demo:${BUILD_ID}"
            }
        }
    }
}


node('slave') {
    container('jnlp-kubectl') {

        stage('Clone YAML') {
            echo "5. Git Clone YAML To Slave"
            git url: "https://gitee.com/cpllouis1127/prometheus-test-demo.git"
        }

        stage('YAML') {
            echo "6. Change YAML File Stage"
            sh 'sed -i "s#{VERSION}#${BUILD_ID}#g" ./jenkins/scripts/prometheus-test-demo.yaml'
        }

        stage('Deploy') {
            echo "7. Deploy To K8s Stage"
            sh 'kubectl apply -f ./jenkins/scripts/prometheus-test-demo.yaml -n nju05'
        }


    }
}

