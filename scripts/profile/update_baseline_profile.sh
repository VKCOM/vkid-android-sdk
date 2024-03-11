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

set -ex
importCommon
assertWorkdirIsClean
checkoutDevelop
BRANCH_NAME="task/VKIDSDK-0/update-baseline-profile"
deleteBranch $BRANCH_NAME
checkoutNewBranch $BRANCH_NAME
generateBaselineProfile
commitCurrent "VKIDSDK-0: Update baseline profile"
createMergeRequest $BRANCH_NAME
