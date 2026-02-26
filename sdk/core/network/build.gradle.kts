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
    api(libs.okhttp3.okhttp)
    implementation(projects.common)
    implementation(projects.logger)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.okhttp3.logging)
    implementation(libs.okhttp3.tls)
    implementation(libs.captcha.okhttp)
    implementation(libs.captcha.core)
    debugImplementation(libs.flipper)
    debugImplementation(libs.flipper.network)

    testImplementation(libs.mockk)
    testImplementation(libs.kotest.core)
    testImplementation(libs.kotest.assertions)
}
