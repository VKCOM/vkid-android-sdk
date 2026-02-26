pluginManagement {
    includeBuild("convention")
    repositories {
        maven(url = "https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-android/")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":metrics")
include(":placeholders")
