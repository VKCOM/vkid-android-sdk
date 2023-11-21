package com.vk.id

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

fun Project.configureKotest(
    android: CommonExtension<*, *, *, *, *>,
) {
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
    dependencies {
        add("testImplementation", libs.findLibrary("kotest.core").get())
        add("testImplementation", libs.findLibrary("kotest.assertions").get())
    }
}