plugins {
    id("vkid.android.library")
    id("vkid.android.publish")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.tracking.noop"
}

dependencies {
    implementation(projects.common)
    implementation(projects.trackingCore)
}