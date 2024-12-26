plugins {
    id("vkid.android.library.compose")
    id("vkid.android.publish")
    id("vkid.dokka")
    id("vkid.binaryCompatibilityValidator")
}

android {
    namespace = "com.vk.id.group.subscription.xml"
}

dependencies {
    implementation(projects.groupSubscriptionCompose)
    implementation(libs.androidx.compose.ui)
}