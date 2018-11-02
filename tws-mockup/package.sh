#!/usr/bin/env bash

mkdir -p ../backend/src/main/resources/static
rm -rf ../backend/src/main/resources/static/
mkdir ../backend/src/main/resources/static
cp -R ./build/ ../backend/src/main/resources/static/