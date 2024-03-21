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
    api(project(":vkid"))
    api(project(":onetap-common"))
    implementation(project(":onetap-compose"))
    implementation(libs.androidx.compose.ui)
    implementation(libs.coil)
}