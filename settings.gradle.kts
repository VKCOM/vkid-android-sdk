pluginManagement {
    includeBuild("build-logic")
    includeBuild("build-logic/detekt")
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
        if (gradle.startParameter.taskNames.map { it.lowercase() }.any { it.contains("dokka") }) {
            mavenLocal()
        }
        val SUBSTITUTE_SAMPLE_PROJECTS_WITH_MODULES: String by settings
        if (SUBSTITUTE_SAMPLE_PROJECTS_WITH_MODULES == "true") {
            maven(url = "https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-andorid/")
            mavenLocal {
                content {
                    includeGroup("com.vk.id")
                }
            }
        }
    }
}
rootProject.name = "VKID"
include(":sample-app")
project(":sample-app").projectDir = File("sample/app")
include(":sample-oldsdk")
project(":sample-oldsdk").projectDir = File("sample/oldsdk")
include(":sample-xml")
project(":sample-xml").projectDir = File("sample/xml")
include(":vkid")
project(":vkid").projectDir = File("sdk/core/vkid")
include(":common")
project(":common").projectDir = File("sdk/core/common")
include(":logger")
project(":logger").projectDir = File("sdk/core/logger")
include(":detekt-rules")
project(":detekt-rules").projectDir = File("sdk/core/detekt-rules")
include(":multibranding-common")
project(":multibranding-common").projectDir = File("sdk/multibranding/multibranding-common")
include(":multibranding-internal")
project(":multibranding-internal").projectDir = File("sdk/multibranding/multibranding-internal")
include(":multibranding-compose")
project(":multibranding-compose").projectDir = File("sdk/multibranding/multibranding-compose")
include(":multibranding-xml")
project(":multibranding-xml").projectDir = File("sdk/multibranding/multibranding-xml")
include(":onetap-common")
project(":onetap-common").projectDir = File("sdk/onetap/onetap-common")
include(":onetap-compose")
project(":onetap-compose").projectDir = File("sdk/onetap/onetap-compose")
include(":onetap-xml")
project(":onetap-xml").projectDir = File("sdk/onetap/onetap-xml")
include(":baseline-profile")
project(":baseline-profile").projectDir = File("sdk/baseline-profile")
include(":ui-tests")
project(":ui-tests").projectDir = File("sdk/ui-tests")
include(":analytics")
project(":analytics").projectDir = File("sdk/core/analytics")
include(":network")
project(":network").projectDir = File("sdk/core/network")
include(":vk-sdk-support")
project(":vk-sdk-support").projectDir = File("sdk/core/vk-sdk-support")
