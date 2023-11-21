plugins {
    id("vkid.android.library.compose")
    id("vkid.android.publish")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

android {
    namespace = "com.vk.id.onetap.xml"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    api(project(":vkid"))
    implementation(project(":onetap-compose"))
    implementation(libs.androidx.compose.ui)
    implementation(libs.coil)
}