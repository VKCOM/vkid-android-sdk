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
    api(projects.vkid)
    implementation(projects.common)
    implementation(projects.trackingCore)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.vk.sdk.core)
}