#!/usr/bin/env sh

set -e

npm i
npm run build

mkdir -p build-web/build
cp -R build/*  build-web/build

cp ci/Dockerfile build-web/Dockerfile
cp ci/default build-web/default