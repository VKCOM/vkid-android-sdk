#!/bin/bash -e

validateCommitMessages() {
    echo "Validating commit message history..."
    COMMIT_MESSAGE_REGEX="(VKIDSDK-[0-9]+: [A-Z][a-zA-Z0-9';,: -]+)"
    git log --pretty=oneline $CI_MERGE_REQUEST_TARGET_BRANCH_SHA..$CI_MERGE_REQUEST_SOURCE_BRANCH_SHA | while read line ; do
        if ! grep -iqE "$COMMIT_MESSAGE_REGEX" <<< "$line"; then
            echo "Commit message must match 'VKIDSDK-XXX: Commit description pattern'"
            exit 1
        fi
    done
    echo "Commit message names are valid"
}

validateBranchName() {
    echo "Validating branch name..."
    BRANCH_NAME_REGEX='((epic|task|fix|feature)\/VKIDSDK-[0-9]+\/([a-zA-Z0-9-]+)*)'
    error_msg="Branch name must match 'type/VKIDSDK-XXX/branch-name'"

    if [[ ! ${CI_MERGE_REQUEST_SOURCE_BRANCH_NAME} =~ ${BRANCH_NAME_REGEX} ]]; then
        echo "$error_msg"
        exit 1
    fi
    echo "Branch name is valid"
}

validateCommitMessages
validateBranchName