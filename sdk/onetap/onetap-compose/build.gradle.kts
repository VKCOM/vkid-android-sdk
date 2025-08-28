plugins {
    id("vkid.android.library.compose")
    id("vkid.android.publish")
    id("vkid.dokka")
    id("vkid.binaryCompatibilityValidator")
    id("vkid.screenshotTesting")
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    namespace = "com.vk.id.onetap.compose"
}

dependencies {
    api(projects.vkid)
    api(projects.multibrandingCompose)
    api(projects.onetapCommon)
    api(projects.groupSubscriptionCompose)
    implementation(projects.analytics)
    implementation(projects.common)
    implementation(projects.multibrandingInternal)
    implementation(projects.trackingCore)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material.ripple)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.compose)
    implementation(libs.androidx.material.ripple)
    api(libs.captcha.okhttp)
    api(libs.captcha.core)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
}