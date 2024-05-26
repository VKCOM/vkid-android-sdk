plugins {
    id("vkid.android.library.compose")
    id("vkid.android.tests")
}

android {
    namespace = "com.vk.id.ui.test"
    packaging {
        resources.excludes.add("**/attach_hotspot_windows.dll")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
        resources.excludes.add("META-INF/licenses/**")
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/LICENSE-notice.md")
    }
}

dependencies {
    androidTestImplementation(projects.vkid)
    androidTestImplementation(projects.onetapXml)
    androidTestImplementation(projects.onetapCompose)
    androidTestImplementation(projects.multibrandingXml)
    androidTestImplementation(projects.multibrandingCompose)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.kotest.core)
    androidTestImplementation(libs.kotest.assertions)
}