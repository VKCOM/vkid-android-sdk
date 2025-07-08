plugins {
    id("vkid.android.library.compose")
    id("vkid.android.publish")
    id("vkid.binaryCompatibilityValidator")
    id("vkid.android.baseline.profile")
    id("vkid.dokka")
}

android {
    namespace = "com.vk.id.common"
}

dependencies {
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
}