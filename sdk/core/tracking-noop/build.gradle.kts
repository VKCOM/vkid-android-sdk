plugins {
    id("vkid.android.library")
    id("vkid.tools.android.publish.library")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.tracking.noop"
}

dependencies {
    implementation(projects.common)
    implementation(projects.trackingCore)
}