plugins {
    id("vkid.android.library")
    id("vkid.android.publish")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.vk.id.multibranding.common"
}

dependencies {
    implementation(project(":vkid"))
    implementation(project(":common"))
    baselineProfile(project(":baselineprofile")) // TODO: Create common plugin or move to existing
}