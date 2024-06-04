plugins {
    id("vkid.android.library")
    id("vkid.android.publish")
    id("vkid.binaryCompatibilityValidator")
    id("vkid.android.baseline.profile")
    id("vkid.dokka")
}

android {
    namespace = "com.vk.id.network"
}

dependencies {
    api(libs.okhttp3.okhttp)
    implementation(projects.common)
    implementation(projects.logger)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.okhttp3.logging)
    debugImplementation(libs.flipper)
    debugImplementation(libs.flipper.network)

    testImplementation(libs.mockk)
    testImplementation(libs.kotest.core)
    testImplementation(libs.kotest.assertions)
}
