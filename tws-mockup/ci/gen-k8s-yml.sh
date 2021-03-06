#!/usr/bin/env sh

genK8SDeploymentFile()
{
    VERSION=2.0.$BUILD_NUMBER
    env=$1
    harborSecret=harbor$1secret
    target_file=deployment.$env.yml

    echo "*********************************************"
    echo "* gen $target_file ..."
    echo "*********************************************"

    cp deployment.yml $target_file
    sed -i "s/DOCKER_VERSION/$VERSION/g" $target_file
    sed -i "s/APP_NAMESPACE/$env/g" $target_file
    sed -i "s/IMAGE_PULL_SECRETS/$harborSecret/g" $target_file
}

genK8SDeploymentFile 'staging'
genK8SDeploymentFile 'production'
