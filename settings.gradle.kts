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
    }
}
rootProject.name = "VKID"
include(":sample-app")
project(":sample-app").projectDir = File("sample/app")
include(":sample-xml")
project(":sample-xml").projectDir = File("sample/xml")
include(":vkid")
include(":common")
include(":common-ui-tests")
include(":multibranding-common")
include(":multibranding-compose")
include(":multibranding-ui-tests")
include(":multibranding-xml")
include(":onetap-common")
include(":onetap-compose")
include(":onetap-ui-tests")
include(":onetap-xml")
include(":baseline-profile")
