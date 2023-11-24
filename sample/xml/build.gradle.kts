plugins {
    id("vkid.android.library")
}

android {
    namespace = "com.vk.id.sample.xml"
}

dependencies {
    implementation(project(":multibranding-xml"))
    implementation(project(":onetap-xml"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
}
