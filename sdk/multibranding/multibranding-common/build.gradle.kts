plugins {
    id("vkid.android.library")
    id("vkid.tools.android.publish.library")
    id("vkid.tools.android.dokka-core")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.multibranding.common"
}

dependencies {
    implementation(projects.vkid)
    implementation(projects.common)
    implementation(libs.androidx.compose.runtime)
}