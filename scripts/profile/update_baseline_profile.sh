#!/bin/bash

# A script that generates new baseline profile and creates a merge request with changes
#
# Usage: ./update_baseline_profile.sh

importCommon() {
    source "$(git rev-parse --show-toplevel)/scripts/common/git.sh"
}

generateBaselineProfile() {
    ./gradlew generateBaselineProfiles
}

RELEASE_VERSION=$1

set -ex
importCommon
assertWorkdirIsClean
if [[ -z $RELEASE_VERSION ]]; then 
    checkoutDevelop
    generateBaselineProfile
    if nothingToCommit; then
        echo "Baseline profile generated nothing"
        exit 0
    fi
    BRANCH_NAME="task/VKIDSDK-0/update-baseline-profile"
    deleteBranch $BRANCH_NAME
    checkoutNewBranch $BRANCH_NAME
    commitCurrent "VKIDSDK-0: Update baseline profile"
    createMergeRequest $BRANCH_NAME
else 
    generateBaselineProfile
    if nothingToCommit; then
        echo "Baseline profile generated nothing"
        exit 0
    fi
    commitCurrent "VKIDSDK-0: Update baseline profile"
    git push origin HEAD
fi