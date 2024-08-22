#!/bin/bash

# A script that tests integration with vk-android-sdk(https://github.com/VKCOM/vk-android-sdk)
# - adds SNAPSHOT suffix to VERSION_NAME in gradle.properties
# - publishes vkid artifacts to maven local: ./gradlew publishToMavenLocal
# - sets SUBSTITUTE_SAMPLE_PROJECTS_WITH_MODULES=true
# - sets DEPEND_ON_VK_SDK=true
# - runs ./gradlew allDependencies --write-locks
# - builds sample
# - undoes all the changes

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

importCommon
CURRENT_VERSION="$(fetchCurrentVersion)"
NEW_VERSION="$CURRENT_VERSION-SNAPSHOT"
bumpVersionInVersionFile "$CURRENT_VERSION" "$NEW_VERSION"
./gradlew publishToMavenLocal
replaceGradleProperty "s/SUBSTITUTE_SAMPLE_PROJECTS_WITH_MODULES=false/SUBSTITUTE_SAMPLE_PROJECTS_WITH_MODULES=true/"
replaceGradleProperty "s/DEPEND_ON_VK_SDK=false/DEPEND_ON_VK_SDK=true/"
./gradlew allDependencies --write-locks
./gradlew :sample-app:assembleDebug
bumpVersionInVersionFile "$NEW_VERSION" "$CURRENT_VERSION"
replaceGradleProperty "s/SUBSTITUTE_SAMPLE_PROJECTS_WITH_MODULES=true/SUBSTITUTE_SAMPLE_PROJECTS_WITH_MODULES=false/"
replaceGradleProperty "s/DEPEND_ON_VK_SDK=true/DEPEND_ON_VK_SDK=false/"
./gradlew allDependencies --write-locks
