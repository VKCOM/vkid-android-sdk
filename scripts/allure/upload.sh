# Uploads allure test run results from allure-results directory
#
# Requirements:
# This script requires allure token to be put to local.properties file and installed utility allurectl https://github.com/allure-framework/allurectl
source "../../local.properties"
export ALLURE_ENDPOINT="https://allure.vk.team"
export ALLURE_TOKEN=$allure_token
export ALLURE_PROJECT_ID="715"
DATE=$(date '+%Y-%m-%d %H:%M:%S')
allurectl upload --launch-name "VKIDSDK Test Launch $DATE" allure-results
