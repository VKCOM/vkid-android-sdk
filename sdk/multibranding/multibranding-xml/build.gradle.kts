plugins {
    id("vkid.android.library.compose")
    id("vkid.android.publish")
    id("vkid.dokka")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.multibranding.xml"
}

dependencies {
    api(projects.vkid)
    api(projects.multibrandingCommon)
    implementation(projects.multibrandingCompose)
    implementation(libs.androidx.compose.ui)
}