plugins {
    id("vkid.android.library")
}

android {
    namespace = "com.vk.id.onetap.ui.tests"
}

dependencies {
    implementation(project(":vkid"))
    implementation(libs.kaspresso)
    implementation(libs.kaspresso.compose)
    implementation(libs.androidx.compose.ui.test.junit4)
    implementation(libs.kotest.assertions)
}