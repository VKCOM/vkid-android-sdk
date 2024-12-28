plugins {
    id("vkid.android.library")
    id("vkid.android.publish")
    id("vkid.dokka")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.group.subscription.common"
}

dependencies {
    api(projects.common)
    implementation(projects.vkid)
    implementation(projects.network)
}