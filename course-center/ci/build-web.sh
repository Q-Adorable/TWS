#!/usr/bin/env sh

set -e

reg_web_path=web/$1
reg_dest=build-web/$2
cd $reg_web_path
npm i
npm run build
cd -

mkdir -p $reg_dest
cp -R $reg_web_path/build/*  $reg_dest



