import com.android.build.api.dsl.ApplicationDefaultConfig
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("vkid.android.application.compose")
}

android {
    namespace = "com.vk.id.sample"

    defaultConfig {
        applicationId = "com.vk.id.sample"
        versionCode = generateVersionCode()
        versionName = properties["VERSION_NAME"] as? String ?: "NO_VERSION"

        setProperty("archivesBaseName", "vkid-$versionName-$versionCode")

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
    implementation(project(":onetap-compose"))
    implementation(project(":onetap-xml"))
    implementation(libs.androidx.activity.compose)
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
    val secrets = Properties()
    secrets.load(FileInputStream(file("secrets.properties")))
    val clientId = secrets["VKIDClientID"] ?: throw IllegalStateException("Add VKIDClientID to file secrets.properties")
    val clientSecret = secrets["VKIDClientSecret"] ?: throw IllegalStateException("Add VKIDClientSecret to file secrets.properties")
    addManifestPlaceholders(mapOf(
        "VKIDRedirectHost" to "vk.com",
        "VKIDRedirectScheme" to "vk$clientId",
        "VKIDClientID" to clientId,
        "VKIDClientSecret" to clientSecret
    ))
}

fun generateVersionCode(): Int {
    return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toInt()
}