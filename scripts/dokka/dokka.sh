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

publishDokkaSkipPlugin() {
    ./gradlew :build-logic:dokka-skip:publishToMavenLocal
}

set -ex
importCommon
assertWorkdirIsClean
if [ "$1" == ""]; then 
    checkoutDevelop
    publishDokkaSkipPlugin
    runDokka
    if nothingToCommit; then
        echo "Dokka has nothing to add"
        exit 0
    fi
    BRANCH_NAME="task/VKIDSDK-0/update-documentation"
    deleteBranch $BRANCH_NAME
    checkoutNewBranch $BRANCH_NAME
    commitCurrent "VKIDSDK-0: Update documentation"
    createMergeRequest $BRANCH_NAME
else 
    BRANCH_NAME="release/$1"
    publishDokkaSkipPlugin
    runDokka
    if nothingToCommit; then
        echo "Dokka has nothing to add"
        exit 0
    fi
    commitCurrent "VKIDSDK-$1: Update documentation"
    git push origin $BRANCH_NAME HEAD
fi