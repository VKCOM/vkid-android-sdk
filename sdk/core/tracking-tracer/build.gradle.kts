import java.util.Properties

plugins {
    id("vkid.android.library")
    id("vkid.android.publish")
    id("vkid.binaryCompatibilityValidator")
    alias(libs.plugins.tracer)
}

android {
    namespace = "com.vk.id.tracking.tracer"
}

tracer {
    create("defaultConfig") {
        val localProperties = Properties().apply { load(rootProject.file("local.properties").inputStream()) }
        // See the "Settings" section
        pluginToken = localProperties.getProperty("tracer.plugin.token")
        appToken = localProperties.getProperty("tracer.app.token")
    }
}

dependencies {
    implementation(projects.common)
    implementation(libs.tracer.lite.crash.reporting)
    implementation(libs.tracer.lite.performance.metrics)
}