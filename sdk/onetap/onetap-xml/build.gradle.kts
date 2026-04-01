plugins {
    id("vkid.android.library.compose")
    id("vkid.tools.android.publish.library")
    id("vkid.tools.android.dokka-core")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.onetap.xml"
}

dependencies {
    api(projects.vkid)
    api(projects.onetapCommon)
    api(projects.groupSubscriptionXml)
    implementation(projects.onetapCompose)
    implementation(libs.androidx.compose.ui)
    api(libs.captcha.core)
    implementation(libs.coil)
}