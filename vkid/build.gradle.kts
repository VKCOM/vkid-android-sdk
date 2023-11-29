plugins {
    id("vkid.android.library")
    id("vkid.android.publish")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

android {
    namespace = "com.vk.id"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        aidl = true
    }
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.okhttp3.okhttp)
    implementation(libs.okhttp3.logging)
    // force safe okio version, not from okhttp3
    implementation(libs.okio)
    testImplementation(libs.mockk)
    testImplementation(libs.kotest.core)
    testImplementation(libs.kotest.assertions)
}