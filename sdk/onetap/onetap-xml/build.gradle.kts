plugins {
    id("vkid.android.library.compose")
    id("vkid.android.publish")
    id("vkid.dokka")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.onetap.xml"
}

dependencies {
    api(projects.vkid)
    api(projects.onetapCommon)
    implementation(projects.onetapCompose)
    implementation(libs.androidx.compose.ui)
    api(libs.captcha.okhttp)
    api(libs.captcha.core)
    implementation(libs.coil)
}