#!/usr/bin/env bash

genK8SDeploymentFile()
{
    VERSION=2.0.$BUILD_NUMBER
    env=$1
    target_file=deployment.$env.yml
    harborSecret=harbor$1secret

    echo "*********************************************"
    echo "* gen $target_file ..."
    echo "*********************************************"

    cp deployment.yml $target_file
    sed -i "s/DOCKER_VERSION/$VERSION/g" $target_file
    sed -i "s/APP_NAMESPACE/$env/g" $target_file
    sed -i "s/HOST_NAME/$2/g" $target_file
    sed -i "s/IMAGE_PULL_SECRETS/$harborSecret/g" $target_file
}

genK8SDeploymentFile 'staging' 'staging-jenkins.thoughtworks.cn'
genK8SDeploymentFile 'production' 'jenkins.thoughtworks.cn'
