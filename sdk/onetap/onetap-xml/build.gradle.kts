plugins {
    id("vkid.android.library.compose")
    id("vkid.android.publish")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
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
    androidTestImplementation(project(":onetap-ui-tests"))
    androidTestImplementation(project(":multibranding-ui-tests"))
}