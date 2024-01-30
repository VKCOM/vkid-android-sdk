plugins {
    id("vkid.android.library")
    id("vkid.android.publish")
    id("vkid.dokka")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

android {
    namespace = "com.vk.id.multibranding.common"
}

dependencies {
    implementation(project(":vkid"))
    implementation(project(":common"))
}