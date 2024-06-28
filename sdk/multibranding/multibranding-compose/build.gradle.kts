plugins {
    id("vkid.android.library.compose")
    id("vkid.android.publish")
    id("vkid.dokka")
    id("vkid.binaryCompatibilityValidator")
    alias(libs.plugins.screenshot)
    id("vkid.screenshotTesting")
}

android {
    namespace = "com.vk.id.multibranding"
}

dependencies {
    api(projects.vkid)
    api(projects.common)
    api(projects.multibrandingCommon)
    implementation(projects.analytics)
    implementation(projects.multibrandingInternal)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material.ripple)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
}