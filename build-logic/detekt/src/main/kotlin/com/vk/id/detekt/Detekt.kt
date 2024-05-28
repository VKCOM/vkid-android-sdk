package com.vk.id.detekt

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

internal fun Project.configureDetekt(isCompose: Boolean) {
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
        val rootDir = rootDir.let { if (it.name == "build-logic") it.parent else it }
        val configs = listOfNotNull(
            "$rootDir/build-logic/detekt/config/detekt.yml",
            if (isCompose) "$rootDir/build-logic/detekt/config/detekt-compose.yml" else null
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
        addProvider("detektPlugins", provider {
            val libs = this@configureDetekt.extensions.getByType<VersionCatalogsExtension>().named("libs")
            libs.findLibrary("detekt-formatting").get().get()
        })
        if (findProject(":detekt-rules") != null) {
            add("detekt", project(":detekt-rules"))
        }
    }
}