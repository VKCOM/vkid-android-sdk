#!/bin/bash

# A script that runs a release build on Teamcity
#
# Prerequisites
# - Add 'vkid_teamcity_token' with user 'vkid_user' to your keychain group 'Auth'('Вход'). The value should be your Teamcity api token.
#
# Usage:
# - ./release_sdk.sh
# - Type password to your mac profile when prompted (can be asked multiple times)
# - (Optional) click 'Always allow'
#
# This script does the following:
# - Checks out develop
# - Reads version from VERSION_NAME property from gradle.properties
# - Launches Release SDK job on branch release/$VERSION_NAME

generate_post_data() {
  cat <<EOF
{
  "branchName": "release/$(fetchCurrentVersion)",
  "personal": true,
  "buildType": {
    "id": "VkIdSdk_AndroidSDK_ReleaseSdk"
  },
  "comment": {
    "text": "Release SDK"
  }
}
EOF
}

startReleaseSdkBuild() {
    curl -i \
        -H "Authorization: Bearer $(getTeamcityToken)" \
        -H "Accept: application/json" \
        -H "Content-Type:application/json" \
        -X POST --data "$(generate_post_data)" "https://teamcity2.mvk.com/app/rest/buildQueue"
}

getTeamcityToken() {
    security find-generic-password -w -s 'vkid_teamcity_token' -a 'vkid_user'
}

checkoutDevelop() {
    git checkout develop
}

importCommon() {
    source "$(git rev-parse --show-toplevel)/scripts/release/release_common.sh"
}

set -e
importCommon
checkoutDevelop
startReleaseSdkBuild
