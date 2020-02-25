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
