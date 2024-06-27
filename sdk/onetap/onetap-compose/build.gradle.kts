plugins {
    id("vkid.android.library.compose")
    id("vkid.android.publish")
    id("vkid.dokka")
    id("vkid.binaryCompatibilityValidator")
    alias(libs.plugins.screenshot)
    id("vkid.screenshotTesting")
}

android {
    namespace = "com.vk.id.onetap.compose"
}

dependencies {
    api(projects.vkid)
    api(projects.multibrandingCompose)
    api(projects.onetapCommon)
    implementation(projects.common)
    implementation(projects.analytics)
    implementation(projects.multibrandingInternal)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material.ripple)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.compose)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
}