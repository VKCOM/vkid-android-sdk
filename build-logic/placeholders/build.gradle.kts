import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    id("vkid.detekt")
    id("vkid.android.plugin.publish")
}


// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        // Force implicit visibility modifiers to avoid mistakes like exposing internal api
        freeCompilerArgs = freeCompilerArgs + "-Xexplicit-api=strict"
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
}

gradlePlugin {
    plugins {
        if (rootProject.property("USE_PLUGINS_FROM_SOURCES") == "true") {
            register("vkidManifestPlaceholders") {
                id = "vkid.manifest.placeholders"
                implementationClass = "com.vk.id.manifest.placeholders.VKIDManifestPlaceholdersPlugin"
            }
        }
    }
}