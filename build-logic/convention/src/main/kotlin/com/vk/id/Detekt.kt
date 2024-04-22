package com.vk.id

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

fun Project.configureDetekt(isCompose: Boolean = false) {
    pluginManager.apply("io.gitlab.arturbosch.detekt")

    tasks.withType<Detekt>().configureEach {
        jvmTarget = "1.8"
        reports {
            html.required.set(true)
            html.outputLocation.set(file("${project.buildDir}/reports/detekt-results-${project.name}.html"))
            txt.required.set(true)
            txt.outputLocation.set(file("${project.buildDir}/reports/detekt-results-${project.name}.txt"))
            xml.required.set(false)
            sarif.required.set(false)
            md.required.set(false)
        }
        buildUponDefaultConfig = true
        allRules = false
        val configs = listOfNotNull(
            "$rootDir/config/detekt.yml",
            if (isCompose) "$rootDir/config/detekt-compose.yml" else null
        )
        config.setFrom(configs)
        autoCorrect = true
        setSource(
            project.files(
                "src/main/kotlin",
                "src/main/java",
                "src/test/kotlin",
                "src/test/java",
                "src/androidTest/kotlin",
                "src/androidTest/java",
            )
        )
    }
    tasks.withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = "1.8"
    }
    dependencies {
        "detektPlugins"(libs.findLibrary("detekt-formatting").get())
        add("detekt", project(":detekt-rules"))
    }
}