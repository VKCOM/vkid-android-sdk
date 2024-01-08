plugins {
    id("vkid.android.library")
    id("vkid.android.publish")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("vkid.android.baseline.profile")
}

android {
    namespace = "com.vk.id.common"
}