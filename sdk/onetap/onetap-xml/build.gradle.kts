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
    api(projects.groupSubscriptionXml)
    implementation(projects.onetapCompose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.coil)
}