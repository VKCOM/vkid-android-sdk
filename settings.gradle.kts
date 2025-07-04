pluginManagement {
    includeBuild("build-logic")
    includeBuild("build-logic/detekt")
    includeBuild("build-logic/convention")
    repositories {
        maven(url = "https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-android/")
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { setUrl("https://artifactory-external.vkpartner.ru/artifactory/maven/") }
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
            maven(url = "https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-android/")
            mavenLocal {
                content {
                    includeGroup("com.vk.id")
                }
            }
        }
        maven { setUrl("https://artifactory-external.vkpartner.ru/artifactory/vk-id-captcha/android/") }
        maven { setUrl("https://artifactory-external.vkpartner.ru/artifactory/maven/") }
    }
}
rootProject.name = "VKIDSDK"
include(":sample-app")
project(":sample-app").projectDir = File("sample/app")
include(":sample-metrics-app")
project(":sample-metrics-app").projectDir = File("sample/metrics-app")
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
include(":group-subscription-common")
project(":group-subscription-common").projectDir = File("sdk/group-subscription/group-subscription-common")
include(":group-subscription-compose")
project(":group-subscription-compose").projectDir = File("sdk/group-subscription/group-subscription-compose")
include(":group-subscription-xml")
project(":group-subscription-xml").projectDir = File("sdk/group-subscription/group-subscription-xml")
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
include(":tracking-core")
project(":tracking-core").projectDir = File("sdk/core/tracking-core")
include(":tracking-tracer")
project(":tracking-tracer").projectDir = File("sdk/core/tracking-tracer")
include(":tracking-noop")
project(":tracking-noop").projectDir = File("sdk/core/tracking-noop")
include(":vk-sdk-support")
project(":vk-sdk-support").projectDir = File("sdk/core/vk-sdk-support")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")