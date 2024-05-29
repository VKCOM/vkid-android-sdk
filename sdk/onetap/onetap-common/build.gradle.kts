plugins {
    id("vkid.android.library")
    id("vkid.android.publish")
    id("vkid.dokka")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.onetap.common"
}

dependencies {
    implementation(projects.common)
    implementation(projects.multibrandingCommon)
    implementation(projects.vkid)
    implementation(libs.androidx.compose.runtime)
}