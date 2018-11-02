#!/usr/bin/env sh

set -e

ci_path=ci
api_path=backend
jar_path=$api_path/build/libs
dest_path=build-api

cd $api_path
./gradlew clean compileJava compileTestJava sonarqube -x test
