package com.vk.id

import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

fun Project.configureStrictMode() {
    with(extensions.getByType<KotlinAndroidProjectExtension>()) {
        compilerOptions {
            // Force implicit visibility modifiers to avoid mistakes like exposing internal api
            freeCompilerArgs.set(listOf("-Xexplicit-api=strict"))
        }
    }
}
