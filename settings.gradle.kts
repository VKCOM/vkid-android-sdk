pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://artifactory.mvk.com/artifactory/maven")
        }
    }
}
rootProject.name = "VKID"
include(":sample-app")
project(":sample-app").projectDir = File("sample/app")
include(":sample-xml")
project(":sample-xml").projectDir = File("sample/xml")
include(":vkid")
include(":multibranding-common")
include(":multibranding-compose")
include(":multibranding-xml")
include(":onetap-compose")
include(":onetap-common")
