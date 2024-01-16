pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven(url = "https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-andorid/")
    }
}
rootProject.name = "VKID"
include(":sample-app")
project(":sample-app").projectDir = File("sample/app")
include(":sample-xml")
project(":sample-xml").projectDir = File("sample/xml")
include(":vkid")
project(":vkid").projectDir = File("sdk/core/vkid")
include(":common")
project(":common").projectDir = File("sdk/core/common")
include(":common-ui-tests")
project(":common-ui-tests").projectDir = File("sdk/core/common-ui-tests")
include(":multibranding-common")
project(":multibranding-common").projectDir = File("sdk/multibranding/multibranding-common")
include(":multibranding-compose")
project(":multibranding-compose").projectDir = File("sdk/multibranding/multibranding-compose")
include(":multibranding-ui-tests")
project(":multibranding-ui-tests").projectDir = File("sdk/multibranding/multibranding-ui-tests")
include(":multibranding-xml")
project(":multibranding-xml").projectDir = File("sdk/multibranding/multibranding-xml")
include(":onetap-common")
project(":onetap-common").projectDir = File("sdk/onetap/onetap-common")
include(":onetap-compose")
project(":onetap-compose").projectDir = File("sdk/onetap/onetap-compose")
include(":onetap-ui-tests")
project(":onetap-ui-tests").projectDir = File("sdk/onetap/onetap-ui-tests")
include(":onetap-xml")
project(":onetap-xml").projectDir = File("sdk/onetap/onetap-xml")
include(":baseline-profile")
project(":baseline-profile").projectDir = File("sdk/baseline-profile")
