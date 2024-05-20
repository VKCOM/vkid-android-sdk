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
    implementation(project(":common"))
    implementation(project(":multibranding-common"))
    implementation(project(":vkid"))
    implementation(libs.androidx.compose.runtime)
}