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
include(":sample")
include(":vkid")
include(":multibranding-compose")
include(":multibranding-xml")
include(":onetap-compose")
include(":onetap-xml")
