plugins {
    id("vkid.android.library.compose")
    id("vkid.android.publish")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
}

android {
    namespace = "com.vk.id.multibranding.xml"
}

dependencies {
    api(project(":vkid"))
    api(project(":multibranding-common"))
    implementation(project(":multibranding-compose"))
    implementation(libs.androidx.compose.ui)
    androidTestImplementation(project(":multibranding-ui-tests"))
}