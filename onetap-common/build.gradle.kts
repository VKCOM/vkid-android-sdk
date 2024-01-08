plugins {
    id("vkid.android.library")
    id("vkid.android.publish")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.vk.id.onetap.common"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":multibranding-common"))
    implementation(project(":vkid"))
    baselineProfile(project(":baselineprofile"))
}