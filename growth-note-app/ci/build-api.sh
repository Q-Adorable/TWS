#!/usr/bin/env sh

set -e

cd backend
./gradlew clean
./gradlew build -x test
cd -

mkdir -p build-api

cp backend/build/libs/backend-0.0.1-SNAPSHOT.jar build-api/app.jar
cp ci/Dockerfile build-api/Dockerfile

