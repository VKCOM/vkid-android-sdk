plugins {
    id("vkid.android.library")
}

android {
    namespace = "com.vk.id.multibranding.ui.tests"
}

dependencies {
    implementation(project(":vkid"))
    implementation(project(":common"))
    api(project(":common-ui-tests"))
    implementation(libs.kaspresso)
    implementation(libs.kaspresso.compose)
    implementation(libs.androidx.compose.ui.test.junit4)
    implementation(libs.kotest.assertions)
}