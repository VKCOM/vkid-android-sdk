#!/bin/bash

# A script that bumps sdk version to the provided one
# Usage: ./bump_version.sh X.Y.Z
#
# This script does the following:
# - Checks that there are no uncommited changes
# - Switches to develop
# - Creates new branch task/VKIDSDK-0/bump-version-to-X-Y-Z from develop
# - Changes VERSION_NAME to X.Y.Z in gradle.properties
# - Creates a commit "VKIDSDK-0: Update version to X.Y.Z"
# - Opens a merge request to develop with branch task/VKIDSDK-0/bump-version-to-X-Y-Z

bumpVersion() {
    NEW_VERSION=$1
    assertValidSemver $NEW_VERSION
    CURRENT_VERSION="$(fetchCurrentVersion)"
    assertNewVersionIsDifferent $CURRENT_VERSION $NEW_VERSION
    bumpVersionInVersionFile $CURRENT_VERSION $NEW_VERSION
    echo "Updated version to $NEW_VERSION"
}

importCommon() {
    source "$(git rev-parse --show-toplevel)/scripts/release/release_common.sh"
}

commitVersionChange() {
    git add -A
    git commit -m "VKIDSDK-0: Update version to $1"
    echo "Commited version change to $1"
    echo "Checked out version change branch"
}

checkoutVersionChangeBranch() {
    git checkout -b task/VKIDSDK-0/bump-version-to-$1
}

createMergeRequest() {
    git push -o merge_request.create --set-upstream origin task/VKIDSDK-0/bump-version-to-$1
    echo "Created merge request with with version change"
}

bumpVersionInVersionFile() {
    VERSION_FILE="$(fetchVersionFile)"
    CURRENT_VERSION=$1
    NEW_VERSION=$2
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
importCommon
assertWorkdirIsClean
checkoutDevelop
checkoutVersionChangeBranch $1
bumpVersion $1
commitVersionChange $1
createMergeRequest $1