#!/bin/bash

# A script that generates documentation using dokka and create a merge request with changes
#
# Usage: ./dokka.sh

importCommon() {
    source "$(git rev-parse --show-toplevel)/scripts/common/git.sh"
}

runDokka() {
    ./gradlew dokkaHtmlMultiModule
}

set -e
importCommon
assertWorkdirIsClean
checkoutDevelop
BRANCH_NAME="task/VKIDSDK-0/update-documentation"
checkoutNewBranch $BRANCH_NAME
runDokka
commitCurrent "VKIDSDK-0: Update documentation"
createMergeRequest $BRANCH_NAME
