plugins {
    id("vkid.android.library.compose")
    id("vkid.tools.android.publish.library")
    id("vkid.tools.android.dokka-core")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.multibranding.xml"
}

dependencies {
    api(projects.vkid)
    api(projects.multibrandingCommon)
    api(projects.groupSubscriptionXml)
    implementation(projects.multibrandingCompose)
    implementation(libs.androidx.compose.ui)
    api(libs.captcha.core)
}
