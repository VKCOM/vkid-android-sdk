plugins {
    id("vkid.android.application.compose")
    id("vkid.manifest.placeholders")
}

android {
    namespace = "com.vk.id.sample.metrics.app"

    defaultConfig {
        applicationId = "com.vk.id.sample.metrics"
        versionCode = 1
        versionName = "1"

        signingConfigs {
            getByName("debug") {
                keyAlias = "androiddebugkey"
                keyPassword = "android"
                storeFile = file("../app/config/debug.keystore")
                storePassword = "android"
            }
        }
    }
    buildTypes {
        create("withSdk") {
            initWith(buildTypes.getByName("debug"))
            matchingFallbacks.add("debug")
        }
        create("withDeps") {
            initWith(buildTypes.getByName("debug"))
            matchingFallbacks.add("debug")
        }
    }
}

dependencies {
    add("withSdkImplementation", projects.vkSdkSupport)
    add("withSdkImplementation", projects.vkid)
    add("withSdkImplementation", projects.multibrandingCompose)
    add("withSdkImplementation", projects.multibrandingXml)
    add("withSdkImplementation", projects.onetapCompose)
    add("withSdkImplementation", projects.onetapXml)

    debugImplementation(libs.flipper)
    debugImplementation(libs.flipper.network)

    add("withDepsImplementation", libs.kotlinx.coroutines.core)
    add("withDepsImplementation", libs.kotlinx.coroutines.android)
    add("withDepsImplementation", platform(libs.androidx.compose.bom))
    add("withDepsImplementation", libs.androidx.activity.compose)
    add("withDepsImplementation", libs.androidx.annotation)
    add("withDepsImplementation", libs.androidx.appcompat)
    add("withDepsImplementation", libs.androidx.browser)
    add("withDepsImplementation", libs.androidx.compose.material3)
    add("withDepsImplementation", libs.androidx.compose.runtime)
    add("withDepsImplementation", libs.androidx.compose.ui)
    add("withDepsImplementation", libs.androidx.compose.foundation)
    add("withDepsImplementation", libs.androidx.compose.material.ripple)
    add("withDepsImplementation", libs.androidx.compose.ui.tooling.preview)
    add("withDepsImplementation", libs.androidx.lifecycle.compose)
    add("withDepsImplementation", libs.androidx.compose.ui.util)
    add("withDepsImplementation", libs.androidx.constraintlayout)
    add("withDepsImplementation", libs.androidx.preference)
    add("withDepsImplementation", libs.androidx.preference.ktx)
    add("withDepsImplementation", libs.androidx.security.crypto)
    add("withDepsImplementation", libs.androidx.core.ktx)
    add("withDepsImplementation", libs.okhttp3.okhttp)
    add("withDepsImplementation", libs.okhttp3.logging)
    add("withDepsImplementation", libs.gson)
    add("withDepsImplementation", libs.coil)
    add("withDepsImplementation", libs.coil.compose)
    add("withDepsImplementation", libs.flipper)
    add("withDepsImplementation", libs.flipper.network)
}
