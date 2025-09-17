plugins {
    id("vkid.android.library.compose")
    id("vkid.android.publish")
    id("vkid.dokka")
    id("vkid.binaryCompatibilityValidator")
    id("vkid.screenshotTesting")
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    namespace = "com.vk.id.group.subscription.compose"
}

dependencies {
    api(projects.vkid)
    api(projects.common)
    api(projects.groupSubscriptionCommon)
    api(projects.analytics)
    implementation(projects.network)
    implementation(projects.trackingCore)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.material3.android)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
    testImplementation(libs.mockk)
}