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
    implementation(project(":vkid"))
    implementation(project(":common"))
    implementation(libs.androidx.compose.runtime)
}