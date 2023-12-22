package com.vk.id

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

fun Project.configureKotest() {
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        systemProperty("kotest.framework.parallelism", 10)
    }
    dependencies {
        add("testImplementation", libs.findLibrary("kotest.core").get())
        add("testImplementation", libs.findLibrary("kotest.assertions").get())
    }
}