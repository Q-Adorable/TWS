#!/usr/bin/env sh

set -e
npm i
npm run test:coverage
npm run sonarqube