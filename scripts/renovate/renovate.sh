#!/bin/bash

# A script that updates dependencies and creates a merge request for it
#
# Prerequisites:
# - Docker daemon must be running, for example Docker Desktop should be launched
# - Add 'vkid_github_token' with user 'vkid_user' to your keychain group 'Вход'. The value should be your Github api token. This is needed, becuase renovate uses Github api.
# - Add 'vkid_gitlab_token' with user 'vkid_user' to your keychain group 'Вход'. The value should be your Gitlab api token. This is needed to clone gitlab project.
#
# Usage: 
# - ./renovate.sh
# - Type password to your mac profile when promted (can be asked multiple times)
# - (Optional) click 'Always allow'

VKID_GITHUB_TOKEN="$(security find-generic-password -w -s 'vkid_github_token' -a 'vkid_user')"
VKID_GITLAB_TOKEN="$(security find-generic-password -w -s 'vkid_gitlab_token' -a 'vkid_user')"
CONFIG_FILE="$(git rev-parse --show-toplevel)/scripts/renovate/config.js"

docker run \
	-it \
	-e GITHUB_COM_TOKEN="$VKID_GITHUB_TOKEN" \
	-e LOG_LEVEL="debug" \
	-v $CONFIG_FILE:/usr/src/app/config.js \
	renovate/renovate \
	--token "$VKID_GITLAB_TOKEN"

