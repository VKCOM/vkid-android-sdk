plugins {
    id("vkid.android.library")
}

android {
    namespace = "com.vk.id.sample.xml"
}

dependencies {
    implementation(project(":multibranding-xml"))
    implementation(project(":onetap-xml"))
    implementation(project(":network"))
    implementation(project(":common"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.preference)
    debugImplementation(libs.flipper)
    debugImplementation(libs.flipper.network)
    debugImplementation(libs.soloader)
    releaseImplementation(libs.flipper.noop)
}
