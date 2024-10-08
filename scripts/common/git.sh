#!/bin/bash

# A set of common scripts for working with Git

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

pushToOrigin() {
    git push origin $(git rev-parse --abbrev-ref HEAD)
}

createMergeRequest() {
    BRANCH_NAME=$1
    git push -o merge_request.create --set-upstream origin "$BRANCH_NAME"
}

checkoutNewBranch() {
    BRANCH_NAME=$1
    git checkout -b "$1"
}

commitCurrent() {
    COMMIT_MESSAGE=$1
    git add -A
    git commit -m "$COMMIT_MESSAGE"
}

deleteBranch() {
    BRANCH_NAME=$1
    git branch -D "$BRANCH_NAME" || true
}

nothingToCommit() {
  if [[ $(git status --porcelain) ]]; then
    false
  else
    true
  fi
}