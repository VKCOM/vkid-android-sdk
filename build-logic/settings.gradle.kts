pluginManagement {
    includeBuild("convention")
    repositories {
        maven { setUrl("https://nexus-external.vkteam.ru/repository/maven/") }
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
