plugins {
    id("vkid.android.library")
    id("vkid.tools.android.publish.library")
    id("vkid.binaryCompatibilityValidator")
    id("vkid.tools.android.baseline-profile")
    id("vkid.tools.android.dokka-core")
}

android {
    namespace = "com.vk.id.network"
}

dependencies {
    implementation(projects.common)
    implementation(projects.logger)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.captcha.core)

    testImplementation(libs.mockk)
    testImplementation(libs.kotest.core)
    testImplementation(libs.kotest.assertions)
}
