plugins {
    id("vkid.android.application.compose")
    id("vkid.placeholders")
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.vk.id.sample.app"

    defaultConfig {
        applicationId = "com.vk.id.sample"
        versionCode = generateVersionCode()
        versionName = properties["VERSION_NAME"] as? String ?: "NO_VERSION"

        setProperty("archivesBaseName", "vkid-${stringProperty("build.type")}-${stringProperty("build.number")}")

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
            isMinifyEnabled = true
            signingConfig = signingConfigs.findByName("debug")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        create("benchmark") {
            initWith(getByName("release"))
            matchingFallbacks.add("release")
            signingConfig = signingConfigs.getByName("debug")
            isShrinkResources = false
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":sample-xml"))
    implementation(project(":multibranding-compose"))
    implementation(project(":multibranding-xml"))
    implementation(project(":onetap-compose"))
    implementation(project(":onetap-xml"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.okhttp3.okhttp)
    implementation(libs.androidx.profileinstaller)
    baselineProfile(project(":baseline-profile"))
    debugImplementation(libs.androidx.compose.ui.tooling)
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
