plugins {
    id("vkid.android.library")
    id("vkid.tools.android.publish.library")
    id("vkid.binaryCompatibilityValidator")
    id("vkid.tools.android.baseline-profile")
    id("vkid.tools.android.dokka-core")
}

android {
    namespace = "com.vk.id.common"
}