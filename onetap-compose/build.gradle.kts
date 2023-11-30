plugins {
    id("vkid.android.library.compose")
    id("vkid.android.publish")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

android {
    namespace = "com.vk.id.onetap.compose"
}

dependencies {
    api(project(":vkid"))
//    api(project(":multibranding-compose"))
    api(project(":onetap-common"))
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