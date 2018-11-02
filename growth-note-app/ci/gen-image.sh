#!/usr/bin/env sh

genImage() {
    IMAGE=52.81.18.101/tws/$2:2.0.$BUILD_NUMBER

    cd $1
    docker build . -t $IMAGE
    docker login 52.81.18.101  -u $USERNAME -p $PASSWORD
    docker push $IMAGE
    docker rmi $IMAGE
    cd -
}


genImage 'build-web' 'tws-growth-note-web'
genImage 'build-api' 'tws-growth-note-api'

