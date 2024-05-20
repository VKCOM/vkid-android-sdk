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
    api(project(":vkid"))
    api(project(":multibranding-common"))
    implementation(project(":multibranding-compose"))
    implementation(libs.androidx.compose.ui)
}