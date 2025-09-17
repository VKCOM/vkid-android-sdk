#!/bin/bash
#
# A script that syncs GitLab's master with GitHub's master
#
# Usage:
# ./sync_with_github.sh
#
# This script does the following
# - Checks out master
# - Reads current version
# - Pushes diff between GitLab's master and GitHub's public branch to public branch on GitHub

importCommon() {
    source "$(git rev-parse --show-toplevel)/scripts/common/versions.sh"
}

set -ex
importCommon
git checkout master
PUBLIC_REPO_URL="git@github.com:VKCOM/vkid-android-sdk.git"
PUBLIC_TARGET_BRANCH="public"
SDK_VERSION="$(fetchCurrentVersion)"
git branch -D public
git checkout -b public
git remote add public ${PUBLIC_REPO_URL}
git fetch public
git reset --soft public/${PUBLIC_TARGET_BRANCH}
git add .
git commit -m "Release v${SDK_VERSION}"
git commit --amend --author="VK ID <devsupport.vk.ru>" --no-edit
git push public HEAD:public