#!/bin/sh

set -e


cd backend
./gradlew clean compileJava compileTestJava sonarqube -x test
