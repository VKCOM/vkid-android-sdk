plugins {
    id("vkid.android.library")
    id("vkid.android.publish")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.multibranding.internal"
}

dependencies {
    implementation(project(":common"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
}