#!/usr/bin/env bash

mkdir $HOME"/.gradle" -p
GRADLE_PROPERTIES=$HOME"/.gradle/gradle.properties"
export GRADLE_PROPERTIES
echo "Gradle Properties should exist at $GRADLE_PROPERTIES"

if [ ! -f "$GRADLE_PROPERTIES" ]; then
    echo "Gradle Properties does not exist"

    echo "Creating Gradle Properties file..."
    touch $GRADLE_PROPERTIES
fi
echo "Writing githubUsername to gradle.properties..."
echo "githubUsername=${1}" >> $GRADLE_PROPERTIES
echo "Writing githubAccessToken to gradle.properties..."
echo "githubAccessToken=${2}" >> $GRADLE_PROPERTIES
echo "Writing verid_artifactory_url to gradle.properties..."
echo "verid_artifactory_url=${3}" >> $GRADLE_PROPERTIES
echo "Writing verid_artifactory_repo to gradle.properties..."
echo "verid_artifactory_repo=${4}" >> $GRADLE_PROPERTIES
echo "Writing verid_artifactory_username to gradle.properties..."
echo "verid_artifactory_username=${5}" >> $GRADLE_PROPERTIES
echo "Writing verid_artifactory_password to gradle.properties..."
echo "verid_artifactory_password=${6}" >> $GRADLE_PROPERTIES
