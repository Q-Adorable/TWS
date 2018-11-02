#!/usr/bin/env sh

set -e

cd web
npm i
npm run build
cd -

mkdir -p build-web/build
cp -R web/build/*  build-web/build

cp ci/web.Dockerfile build-web/Dockerfile
cp ci/default build-web/default
