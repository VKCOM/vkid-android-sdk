/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vk.id

import com.vk.id.util.android
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

/**
 * Configure base Kotlin with Android options
 */
internal fun Project.configureKotlinAndroid() {
    extensions.android {
        compileSdk = Versions.compileSdk

        defaultConfig {
            minSdk = Versions.minSdk

            buildFeatures.buildConfig = true
            buildConfigField("Integer", "CI_BUILD_NUMBER", stringProperty("build.number", "-1"))
            buildConfigField("String", "CI_BUILD_TYPE", "\"${stringProperty("build.type")}\"")
            buildConfigField("String", "VKID_VERSION_NAME", "\"${stringProperty("VERSION_NAME", "NO_VERSION")}\"")
            buildConfigField("Long", "VKID_BUILD_TIME", "${System.currentTimeMillis()}L")
        }

        compileOptions {
            // Up to Java 11 APIs are available through desugaring
            // https://developer.android.com/studio/write/java11-minimal-support-table
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
            isCoreLibraryDesugaringEnabled = true
        }
    }

    configureKotlin()

    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())
    }
}

/**
 * Configure base Kotlin options
 */
private fun Project.configureKotlin() {
    with(extensions.getByType<KotlinAndroidProjectExtension>()) {
        compilerOptions {
            // Set JVM target to 11
            jvmTarget.set(JvmTarget.JVM_11)
            // Treat all Kotlin warnings as errors (disabled by default)
            // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
            val warningsAsErrors: String? by project
            allWarningsAsErrors.set(warningsAsErrors.toBoolean())
        }
    }
}
