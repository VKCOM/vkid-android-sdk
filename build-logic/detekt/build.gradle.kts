import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.vk.id.detekt"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.detekt.gradlePlugin)
}
gradlePlugin {
    plugins {
        register("detekt") {
            id = "vkid.detekt"
            implementationClass = "com.vk.id.detekt.VKIDDetektPlugin"
        }
        register("detektCompose") {
            id = "vkid.detekt.compose"
            implementationClass = "com.vk.id.detekt.VKIDDetektComposePlugin"
        }
    }
}
