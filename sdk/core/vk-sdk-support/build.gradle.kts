plugins {
    id("vkid.android.library")
    id("vkid.android.publish")
    id("vkid.dokka")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.vksdksupport"
}

dependencies {
    api(project(":vkid"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.vk.sdk.core)
}