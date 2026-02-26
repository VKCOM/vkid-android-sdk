plugins {
    id("vkid.android.library")
    id("vkid.tools.android.publish.library")
    id("vkid.tools.android.dokka-core")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.group.subscription.common"
}

dependencies {
    api(projects.common)
    implementation(projects.vkid)
    implementation(projects.network)
    implementation(libs.androidx.compose.runtime)
}