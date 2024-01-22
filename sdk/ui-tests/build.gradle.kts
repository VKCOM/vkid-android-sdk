plugins {
    id("vkid.android.library.compose")
    id("vkid.android.tests")
}

android {
    namespace = "com.vk.id.ui.test"
}

dependencies {
    androidTestImplementation(project(":vkid"))
    androidTestImplementation(project(":onetap-xml"))
    androidTestImplementation(project(":onetap-compose"))
    androidTestImplementation(project(":onetap-ui-tests"))
    androidTestImplementation(project(":multibranding-xml"))
    androidTestImplementation(project(":multibranding-compose"))
    androidTestImplementation(project(":multibranding-ui-tests"))
}