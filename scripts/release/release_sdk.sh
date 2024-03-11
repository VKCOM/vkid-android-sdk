#!/bin/bash

# A script that runs a release build on Teamcity
#
# Prerequisites
# - Add 'vkid_teamcity_token' with user 'vkid_user' to your keychain group 'Auth'('Вход'). The value should be your Teamcity api token.
# - Add 'vkid_teamcity_url' with user 'vkid_user' to to your keychain group 'Auth'('Вход'). The vallu should be the teamcity host.
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
        -X POST --data "$(generate_post_data)" "https://$(getTeamcityUrl)/app/rest/buildQueue"
}

getTeamcityUrl() {
    security find-generic-password -w -s 'vkid_teamcity_url' -a 'vkid_user'
}

getTeamcityToken() {
    security find-generic-password -w -s 'vkid_teamcity_token' -a 'vkid_user'
}

importCommon() {
    source "$(git rev-parse --show-toplevel)/scripts/common/versions.sh"
    source "$(git rev-parse --show-toplevel)/scripts/common/git.sh"
}

set -e
importCommon
checkoutDevelop
startReleaseSdkBuild
