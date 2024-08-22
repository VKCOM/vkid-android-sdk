#!/bin/bash

# A script that bumps sdk version to the provided one
# Usage: ./bump_version.sh X.Y.Z
#
# This script does the following:
# - Checks that there are no uncommitted changes
# - Switches to develop
# - Creates new branch task/VKIDSDK-0/bump-version-to-X-Y-Z from develop
# - Changes VERSION_NAME to X.Y.Z in gradle.properties
# - Creates a commit "VKIDSDK-0: Update version to X.Y.Z"
# - Opens a merge request to develop with branch task/VKIDSDK-0/bump-version-to-X-Y-Z

bumpVersion() {
    NEW_VERSION=$1
    assertValidSemver "$NEW_VERSION"
    CURRENT_VERSION="$(fetchCurrentVersion)"
    assertNewVersionIsDifferent "$CURRENT_VERSION" "$NEW_VERSION"
    bumpVersionInVersionFile "$CURRENT_VERSION" "$NEW_VERSION"
    echo "Updated version to $NEW_VERSION"
}

importCommon() {
    source "$(git rev-parse --show-toplevel)/scripts/common/versions.sh"
    source "$(git rev-parse --show-toplevel)/scripts/common/git.sh"
}

commitVersionChange() {
    commitCurrent "VKIDSDK-0: Update version to $1"
    echo "Committed version change to $1"
    echo "Checked out version change branch"
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

assertNewVersionIsDifferent() {
    CURRENT_VERSION=$1
    NEW_VERSION=$2
    if [ "$NEW_VERSION" == "$CURRENT_VERSION" ]; then
        echo "ERROR: New version is the same as the current version"
        exit 1
    fi
}

assertValidSemver() {
    if ! isValidSemver "$1"; then
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

createVersionMergeRequest() {
    BRANCH_NAME=$1
    createMergeRequest "$BRANCH_NAME"
    echo "Created merge request with with version change"
}

set -ex
importCommon
assertWorkdirIsClean
if [[ $(git rev-parse --abbrev-ref HEAD) =~ ^(release/.*)$ ]]; then
    bumpVersion "$1"
    commitVersionChange "$1"
    pushToOrigin
else
    BRANCH_NAME="task/VKIDSDK-0/bump-version-to-$1"
    checkoutNewBranch "$BRANCH_NAME"
    bumpVersion "$1"
    commitVersionChange "$1"
    createVersionMergeRequest "$BRANCH_NAME"
fi
