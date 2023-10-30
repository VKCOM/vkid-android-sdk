import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import java.io.FileInputStream
import java.util.Properties
import java.io.FileNotFoundException

plugins {
    id("vkid.android.application.compose")
}

android {
    namespace = "com.vk.id.sample"

    defaultConfig {
        applicationId = "com.vk.id.sample"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    initVKID()
}

dependencies {
    implementation(project(":vkid"))
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.core.ktx)
    androidTestImplementation(libs.tests.ui.junit)
    debugImplementation(libs.tests.ui.manifest)

    debugImplementation(libs.androidx.compose.ui.tooling)
}

fun BaseAppModuleExtension.initVKID() {
    defaultConfig {
        tasks.register("initVKID") {
            val secrets = Properties()
            try {
                secrets.load(FileInputStream(file("secrets.properties")))

                val clientId = secrets["VKIDClientID"] ?: throw IllegalStateException("Add VKIDClientID to file secrets.properties")
                val clientSecret = secrets["VKIDClientSecret"] ?: throw IllegalStateException("Add VKIDClientSecret to file secrets.properties")
                addManifestPlaceholders(
                    mapOf(
                        "VKIDRedirectHost" to "vk.com",
                        "VKIDRedirectScheme" to "vk$clientId",
                        "VKIDClientID" to clientId,
                        "VKIDClientSecret" to clientSecret
                    )
                )
            } catch (e: FileNotFoundException) {
                logger.error(
                    "Create a 'secrets.properties' file in the 'sample' folder and add your 'VKIDClientID' and 'VKIDClientSecret' to it." +
                            "\nFor more information, refer to the 'README.md' file."
                )
                throw e
            }
        }

        tasks.whenTaskAdded {
            if (name.startsWith("processDebugManifest") || (name.startsWith("processReleaseManifest"))) {
                dependsOn("initVKID")
            }
        }
    }
}
