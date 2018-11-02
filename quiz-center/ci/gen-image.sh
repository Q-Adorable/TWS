#!/usr/bin/env bash

genImage() {
    IMAGE=52.81.18.101/tws/$2:2.0.$BUILD_NUMBER

    cd $1
    sudo docker build . -t $IMAGE
    sudo docker login 52.81.18.101  -u $USERNAME -p $PASSWORD
    sudo docker push $IMAGE
    sudo docker rmi $IMAGE
    cd -
}


genImage 'build-web' 'tws-quiz-center-web'
genImage 'build-api' 'tws-quiz-center-api'

