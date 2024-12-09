plugins {
    id("vkid.android.library")
    id("vkid.android.publish")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.tracking.core"
}

dependencies {
    implementation(projects.common)
}