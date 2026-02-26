plugins {
    id("vkid.android.library.compose")
    id("vkid.tools.android.publish.library")
    id("vkid.tools.android.dokka-core")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.group.subscription.xml"
}

dependencies {
    api(projects.groupSubscriptionCompose)
    implementation(libs.androidx.compose.ui)
    api(libs.androidx.material3.android)
}