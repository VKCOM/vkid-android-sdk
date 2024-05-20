plugins {
    id("vkid.android.library.compose")
}

android {
    namespace = "com.vk.id.sample.oldsdk"
}

dependencies {
    implementation(project(":sample-xml"))
    implementation(project(":vk-sdk-support"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.vk.sdk.api)
    implementation(libs.vk.sdk.core)
}
