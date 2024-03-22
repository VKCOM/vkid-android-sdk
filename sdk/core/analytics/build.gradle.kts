plugins {
    id("vkid.android.library")
    id("vkid.android.publish")
    id("vkid.dokka")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.analytics"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":network"))
    implementation(project(":logger"))
    implementation(libs.kotlinx.coroutines.core)
}