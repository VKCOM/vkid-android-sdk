plugins {
    id("vkid.android.library.compose")
    id("vkid.android.publish")
    id("vkid.dokka")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.multibranding"
}

dependencies {
    api(project(":vkid"))
    api(project(":common"))
    api(project(":multibranding-common"))
    implementation(project(":analytics"))
    implementation(project(":multibranding-internal"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material.ripple)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
}