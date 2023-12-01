import com.android.build.api.dsl.ApplicationDefaultConfig
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Properties

plugins {
    id("vkid.android.application.compose")
}

android {
    namespace = "com.vk.id.sample.app"

    defaultConfig {
        applicationId = "com.vk.id.sample"
        versionCode = generateVersionCode()
        versionName = properties["VERSION_NAME"] as? String ?: "NO_VERSION"

        setProperty("archivesBaseName", "vkid-${stringProperty("build.type")}-${stringProperty("build.number")}")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        initVKID()

        signingConfigs {
            getByName("debug") {
                keyAlias = "androiddebugkey"
                keyPassword = "android"
                storeFile = file("config/debug.keystore")
                storePassword = "android"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.findByName("debug")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":sample-xml"))
    implementation(project(":multibranding-compose"))
    implementation(project(":onetap-compose"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.coroutines.android)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

fun ApplicationDefaultConfig.initVKID() {
    if (gradle.startParameter.taskNames.any { it.contains("assemble") || it.contains("test") }) {
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
                "Warning! Sample would not work!\nCreate the 'secrets.properties' file in the 'sample/app' folder and add your 'VKIDClientID' and 'VKIDClientSecret' to it." +
                        "\nFor more information, refer to the 'README.md' file."
            )
        }
    }
}

fun generateVersionCode(): Int {
    return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toInt()
}

fun Project.stringProperty(key: String, default: String = ""): String {
    var prop = (properties[key] as? String)?.takeIf { it.isNotEmpty() }
    if (prop == null) {
        prop = (rootProject.properties[key] as? String)?.takeIf { it.isNotEmpty() }
    }
    return prop ?: default
}
