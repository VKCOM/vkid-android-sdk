#!/bin/bash

# A script that generates documentation using dokka and create a merge request with changes
#
# Usage: ./dokka.sh

importCommon() {
    source "$(git rev-parse --show-toplevel)/scripts/common/git.sh"
}

deleteBranch() {
    BRANCH_NAME=$1
    git branch -D $BRANCH_NAME
}

runDokka() {
    ./gradlew dokkaHtmlMultiModule
}

publishDokkaSkipPlugin() {
    ./gradlew :build-logic:dokka-skip:publishToMavenLocal
}

set -e
importCommon
assertWorkdirIsClean
checkoutDevelop
BRANCH_NAME="task/VKIDSDK-0/update-documentation"
deleteBranch $BRANCH_NAME
checkoutNewBranch $BRANCH_NAME
publishDokkaSkipPlugin
runDokka
commitCurrent "VKIDSDK-0: Update documentation"
createMergeRequest $BRANCH_NAME
