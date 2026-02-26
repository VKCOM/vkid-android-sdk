plugins {
    id("vkid.android.library")
    id("vkid.tools.android.publish.library")
    id("vkid.tools.android.dokka-core")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.analytics"
}

dependencies {
    implementation(projects.common)
    implementation(projects.network)
    implementation(projects.logger)
    implementation(libs.kotlinx.coroutines.core)
}