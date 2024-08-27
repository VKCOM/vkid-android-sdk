#!/bin/bash

# Common utility function for release scripts

fetchVersionFile() {
    echo "$(git rev-parse --show-toplevel)/gradle.properties"
}

fetchCurrentVersion() {
    grep -Eo 'VERSION_NAME=.*' "$(fetchVersionFile)" | awk -F'=' '{print $2}'
}

bumpVersionInVersionFile() {
    VERSION_FILE="$(fetchVersionFile)"
    CURRENT_VERSION=$1
    NEW_VERSION=$2
    if [[ "$OSTYPE" == "darwin"* ]]; then
        sed -i '' "s/$CURRENT_VERSION/$NEW_VERSION/" "$VERSION_FILE"
    else
        sed -i "s/$CURRENT_VERSION/$NEW_VERSION/" "$VERSION_FILE"
    fi
}
