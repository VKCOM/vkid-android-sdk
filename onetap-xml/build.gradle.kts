plugins {
    id("vkid.android.library")
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
    api(project(":onetap-common"))
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil)
}