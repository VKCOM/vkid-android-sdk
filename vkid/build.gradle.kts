plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.vk.id"
    compileSdk = 33

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        val clientId = findProperty("VKIDSDK_CLIENT_ID") as? String
            ?: throw IllegalStateException("Cant found gradle property VKIDSDK_CLIENT_ID, you should set it in gradle.properties file.")
        val clientSecret = findProperty("VKIDSDK_CLIENT_SECRET") as? String
            ?: throw IllegalStateException("Cant found gradle property VKIDSDK_CLIENT_SECRET, you should set it in gradle.properties file.")

        val redirectHost = findProperty("VKIDSDK_REDIRECT_HOST") as? String ?: "vk.com"
        val redirectSchemePrefix = findProperty("VKIDSDK_REDIRECT_SCHEME_PREFIX") as? String ?: "vk"
        val redirectScheme = redirectSchemePrefix + clientId
        addManifestPlaceholders(mapOf(
            "VKIDRedirectHost" to redirectHost,
            "VKIDRedirectScheme" to redirectScheme,
        ))

        buildConfigField("String", "VKIDSDK_CLIENT_ID", "\"$clientId\"")
        buildConfigField("String", "VKIDSDK_CLIENT_SECRET", "\"$clientSecret\"")
        buildConfigField("String", "VKIDSDK_REDIRECT_URL", "\"$redirectScheme://$redirectHost\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        // Make all methods private by default
        freeCompilerArgs = freeCompilerArgs + "-Xexplicit-api=strict"
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    resourcePrefix("vkid_")
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.okhttp3.okhttp)
    testImplementation(libs.mockk)
}