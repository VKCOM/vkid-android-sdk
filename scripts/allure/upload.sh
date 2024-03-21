# Uploads allure test run results from allure-results directory
#
# Requirements:
# This script requires several credentials to be put to keychain:
# - vkid_allure_url with allure url in form https://some.allure.url
# - vkid_allure_token with allure token
# - vkid_allure_project with allure project id

export ALLURE_ENDPOINT=$(security find-generic-password -w -s 'vkid_allure_url' -a 'vkid_user')
export ALLURE_TOKEN=$(security find-generic-password -w -s 'vkid_allure_token' -a 'vkid_user')
export ALLURE_PROJECT_ID=$(security find-generic-password -w -s 'vkid_allure_project' -a 'vkid_user')
DATE=$(date '+%Y-%m-%d %H:%M:%S')
allurectl upload --launch-name "VKIDSDK Test Launch $DATE" allure-results
