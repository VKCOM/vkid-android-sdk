#!/bin/bash -e

validateCommitMessages() {
    TARGET_BRANCH=$1
    SOURCE_BRANCH=$2
    echo "Validating commit message history..."
    COMMIT_MESSAGE_REGEX="((VKIDSDK-[0-9]+: [A-Z][a-zA-Z0-9';,: -]+))|(Merge branch .*)"

    SOURCE_BRANCH_SHA=$(git log -n 1 --pretty=format:"%H" $SOURCE_BRANCH)
    TARGET_BRANCH_SHA=$(git merge-base $TARGET_BRANCH $SOURCE_BRANCH)

    git log --pretty=oneline $TARGET_BRANCH_SHA..$SOURCE_BRANCH_SHA | while read line ; do
        if ! grep -iqE "$COMMIT_MESSAGE_REGEX" <<< "$line"; then
            echo "Commit message "$line" must match 'VKIDSDK-XXX: Commit description pattern or be a merge commit'"
            exit 1
        fi
    done
    echo "Commit message names are valid"
}

validateBranchName() {
    SOURCE_BRANCH=$1
    echo "Validating branch name..."
    BRANCH_NAME_REGEX='((epic|task|fix|feature)\/VKIDSDK-[0-9]+\/([a-zA-Z0-9-]+)*|(release/.*)|(master))'
    error_msg="Branch name must match 'type/VKIDSDK-XXX/branch-name'"

    if [[ ! ${SOURCE_BRANCH} =~ ${BRANCH_NAME_REGEX} ]]; then
        echo "$error_msg"
        exit 1
    fi
    echo "Branch name is valid"
}

TARGET_BRANCH=$1
SOURCE_BRANCH=$2
git fetch origin $TARGET_BRANCH:$TARGET_BRANCH || true
git fetch origin $SOURCE_BRANCH:$SOURCE_BRANCH || true
validateCommitMessages $TARGET_BRANCH $SOURCE_BRANCH
validateBranchName $SOURCE_BRANCH