import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.vk.id.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.kotlin.binaryCompatibilityValidator)
    implementation(libs.dokka.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "vkid.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "vkid.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "vkid.android.library"
            implementationClass = "VKIDLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "vkid.android.library.compose"
            implementationClass = "VKIDLibraryComposeConventionPlugin"
        }
        register("androidTests") {
            id = "vkid.android.tests"
            implementationClass = "AndroidTestsConventionPlugin"
        }
        register("vkidPublish") {
            id = "vkid.android.publish"
            implementationClass = "VKIDPublishConventionPlugin"
        }
        register("vkidPluginPublish") {
            id = "vkid.android.plugin.publish"
            implementationClass = "VKIDPluginPublishConventionPlugin"
        }
        register("vkidDependencyLock") {
            id = "vkid.android.dependency.lock"
            implementationClass = "DependencyLockPlugin"
            description = """Helper to create lockfiles for all modules with one command. 
                |Usage: `./gradlew allDependencies --write-locks`""".trimMargin()
        }
        register("vkidBaselineProfile") {
            id = "vkid.android.baseline.profile"
            implementationClass = "BaselineProfilePlugin"
            description = """Helper to create baseline profile for all modules with one command. 
                |Usage: `./gradlew generateBaselineProfiles`""".trimMargin()
        }
        register("vkidProjectSubstitution") {
            id = "vkid.android.project-substitution"
            implementationClass = "ProjectSubstitutionPlugin"
            description = """A plugin that replaces sample project dependencies with module dependencies. 
                |Usage: Add `SUBSTITUTE_SAMPLE_PROJECTS_WITH_MODULES=true` to root gradle.properties""".trimMargin()
        }
        register("vkidDokka") {
            id = "vkid.dokka"
            implementationClass = "VKIDDokkaPlugin"
            description = """A plugin that configures dokka for documentation generation. 
                |Usage: ./gradlew dokkaHtmlMultiModule""".trimMargin()
        }
        register("vkidBinaryCompatibilityValidator") {
            id = "vkid.binaryCompatibilityValidator"
            implementationClass = "VKIDBinaryCompatibilityValidatorPlugin"
        }
        register("vkidScreenshotTesting") {
            id = "vkid.screenshotTesting"
            implementationClass = "VKIDScreenshotTestingPlugin"
        }
    }
}

