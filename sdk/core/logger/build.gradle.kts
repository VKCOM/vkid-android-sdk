plugins {
    id("vkid.android.library")
    id("vkid.android.publish")
    id("vkid.binaryCompatibilityValidator")
    id("vkid.android.baseline.profile")
    id("vkid.dokka")
}

android {
    namespace = "com.vk.id.logger"
}

dependencies {
    implementation(project(":common"))

    testImplementation(libs.mockk)
    testImplementation(libs.kotest.core)
    testImplementation(libs.kotest.assertions)
}