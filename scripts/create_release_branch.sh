#!/bin/bash

bumpVersion() {
    NEW_VERSION=$1
    assertValidSemver $NEW_VERSION
    VERSION_FILE="$(git rev-parse --show-toplevel)/gradle.properties"
    CURRENT_VERSION="$(fetchCurrentVersion $VERSION_FILE)"
    assertNewVersionIsDifferent $CURRENT_VERSION $NEW_VERSION
    bumpVersionInVersionFile $VERSION_FILE $CURRENT_VERSION $NEW_VERSION
}

commitVersionChange() {
    git add -A
    git commit -m "VKIDSDK-0: Update version to $1"
    echo "Commited version change to $1"
}

checkoutVersionChangeBranch() {
    git checkout -b task/VKIDSDK-0/bump-version-to-$1
}

createMergeRequest() {
    git push -o merge_request.create --set-upstream origin task/VKIDSDK-0/bump-version-to-$1
    echo "Created merge request with with version change"
}

fetchCurrentVersion() {
    echo $(grep -Eo 'VERSION_NAME=.*' "$1") | awk -F'=' '{print $2}'
}

bumpVersionInVersionFile() {
    VERSION_FILE=$1
    CURRENT_VERSION=$2
    NEW_VERSION=$3
    sed -i '' "s/$CURRENT_VERSION/$NEW_VERSION/" "$VERSION_FILE"
}

assertWorkdirIsClean() {
    if [ -z "$(git status --porcelain)" ]; then
        # Working directory clean
        echo "Workdir is clean"
    else
        echo "ERROR: You have uncommited changes"
        exit 1
    fi
}

checkoutDevelop() {
    git checkout develop
}

assertNewVersionIsDifferent() {
    CURRENT_VERSION=$1
    NEW_VERSION=$2
    if [ "$NEW_VERSION" == "$CURRENT_VERSION" ]; then
        echo "ERROR: New version is the same as the current version"
        exit 1
    fi
}

assertValidSemver() {
    if ! isValidSemver $1; then
        echo "ERROR: $1 isn't a valid semantic versioning"
        exit 1
    fi
}

isValidSemver() {
    if ! [[ ${1:-} =~ ^([0-9]{1}|[1-9][0-9]+)\.([0-9]{1}|[1-9][0-9]+)\.([0-9]{1}|[1-9][0-9]+)($|[-+][0-9A-Za-z+.-]+$) ]]; then
        false
        return
    fi
}

set -e
assertWorkdirIsClean
checkoutDevelop
checkoutVersionChangeBranch $1
bumpVersion $1
commitVersionChange $1
createMergeRequest $1
