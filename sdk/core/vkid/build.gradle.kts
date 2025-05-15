plugins {
    id("vkid.android.library.compose")
    id("vkid.android.publish")
    id("vkid.dokka")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        aidl = true
    }
}

dependencies {
    implementation(projects.common)
    implementation(projects.analytics)
    implementation(projects.network)
    implementation(projects.logger)
    implementation(projects.trackingCore)
    implementation(projects.trackingTracer)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.security.crypto)
    api(libs.captcha.okhttp)
    api(libs.captcha.core)
    implementation(libs.gson)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.vk.userid)
    testImplementation(libs.mockk)
    testImplementation(libs.kotest.core)
    testImplementation(libs.kotest.assertions)
    testImplementation(kotlin("reflect"))
}