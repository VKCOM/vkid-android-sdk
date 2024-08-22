#!/bin/bash

# A script that tests integration with vk-android-sdk(https://github.com/VKCOM/vk-android-sdk)
# - adds SNAPSHOT suffix to VERSION_NAME in gradle.properties
# - publishes vkid artifacts to maven local: ./gradlew publishToMavenLocal
# - sets SUBSTITUTE_SAMPLE_PROJECTS_WITH_MODULES=true
# - sets DEPEND_ON_VK_SDK=true
# - runs ./gradlew allDependencies --write-locks
# - builds sample
# - undoes all the changes
#
# Contract:
# The first parameter is test mode
# "./sample-assembling-integration 0" Tests integration with vk sdk
# "./sample-assembling-integration 1" Tests plain sample assembling with sdk from maven local
# "./sample-assembling-integration 2" Tests sample assembling with sdk from artifactory

set -e

importCommon() {
    source "$(git rev-parse --show-toplevel)/scripts/common/versions.sh"
}

replaceGradleProperty() {
    REGEX=$1
    VERSION_FILE="$(fetchVersionFile)"
    if [[ "$OSTYPE" == "darwin"* ]]; then
        sed -i '' $REGEX "$VERSION_FILE"
    else
        sed -i $REGEX "$VERSION_FILE"
    fi
}

MODE=$1

importCommon
CURRENT_VERSION="$(fetchCurrentVersion)"
NEW_VERSION="$CURRENT_VERSION-SNAPSHOT"
if [ "$MODE" -ne 2 ]; then
    bumpVersionInVersionFile "$CURRENT_VERSION" "$NEW_VERSION"
    ./gradlew publishToMavenLocal
fi
replaceGradleProperty "s/SUBSTITUTE_SAMPLE_PROJECTS_WITH_MODULES=false/SUBSTITUTE_SAMPLE_PROJECTS_WITH_MODULES=true/"
if [ "$MODE" -eq 0 ]; then
    replaceGradleProperty "s/DEPEND_ON_VK_SDK=false/DEPEND_ON_VK_SDK=true/"
fi
./gradlew allDependencies --write-locks
./gradlew :sample-app:assembleDebug
if [ "$MODE" -ne 2 ]; then
    bumpVersionInVersionFile "$NEW_VERSION" "$CURRENT_VERSION"
    replaceGradleProperty "s/SUBSTITUTE_SAMPLE_PROJECTS_WITH_MODULES=true/SUBSTITUTE_SAMPLE_PROJECTS_WITH_MODULES=false/"
fi
if [ "$MODE" -eq 0 ]; then
    replaceGradleProperty "s/DEPEND_ON_VK_SDK=true/DEPEND_ON_VK_SDK=false/"
fi
./gradlew allDependencies --write-locks
