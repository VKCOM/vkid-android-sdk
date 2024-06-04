plugins {
    id("vkid.android.library")
    id("vkid.android.publish")
    id("vkid.dokka")
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