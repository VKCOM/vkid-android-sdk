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
    androidTestImplementation(projects.network)
    androidTestImplementation(projects.onetapCompose)
    androidTestImplementation(projects.onetapXml)
    androidTestImplementation(projects.multibrandingCompose)
    androidTestImplementation(projects.multibrandingXml)
    androidTestImplementation(projects.groupSubscriptionCompose)
    androidTestImplementation(projects.groupSubscriptionXml)
    androidTestImplementation(libs.espresso.intents)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.kotest.core)
    androidTestImplementation(libs.kotest.assertions)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.activity.compose)
    androidTestImplementation(libs.androidx.compose.material3)

}