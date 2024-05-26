plugins {
    id("vkid.android.library")
}

android {
    namespace = "com.vk.id.sample.xml"
}

dependencies {
    implementation(projects.multibrandingXml)
    implementation(projects.onetapXml)
    implementation(projects.network)
    implementation(projects.common)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.preference)
    debugImplementation(libs.flipper)
    debugImplementation(libs.flipper.network)
    debugImplementation(libs.soloader)
    releaseImplementation(libs.flipper.noop)
}
