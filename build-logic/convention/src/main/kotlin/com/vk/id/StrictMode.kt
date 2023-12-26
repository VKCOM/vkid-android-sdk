package com.vk.id

import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.configureStrictMode() {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            // Force implicit visibility modifiers to avoid mistakes like exposing internal api
            freeCompilerArgs = freeCompilerArgs + "-Xexplicit-api=strict"
        }
    }
}
