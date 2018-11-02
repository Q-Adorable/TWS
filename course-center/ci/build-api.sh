#!/usr/bin/env sh

set -e

ci_path=ci
api_path=backend
jar_path=$api_path/build/libs
dest_path=build-api

cd $api_path
./gradlew build -x test
cd -
mkdir -p $dest_path
cp $jar_path/backend-0.0.1-SNAPSHOT.jar $dest_path/app.jar

cp $ci_path/Dockerfile $dest_path

