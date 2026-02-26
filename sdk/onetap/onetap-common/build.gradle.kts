plugins {
    id("vkid.android.library")
    id("vkid.tools.android.publish.library")
    id("vkid.tools.android.dokka-core")
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