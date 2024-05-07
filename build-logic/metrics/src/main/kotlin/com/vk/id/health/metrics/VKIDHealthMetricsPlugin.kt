package com.vk.id.health.metrics

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import java.io.ByteArrayOutputStream
import java.io.IOException

internal class VKIDHealthMetricsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("healthMetrics", VKIDHealthMetricsExtension::class)
        extension.rootProjectInternal = target.rootProject
        val publishMetricsTask = target.tasks.create("publishHealthMetrics") {
            doLast {
                println(extension.steps.joinToString("\n") { it.getDiff() })
            }
        }
        val calculateMetricsTask = target.tasks.create("calculateHealthMetrics") {
            doLast {
                extension.steps.forEach {
                    target.exec {
                        workingDir = project.projectDir
                        commandLine("./gradlew", it.task.name, "--stacktrace")
                    }
                }
            }
        }
        publishMetricsTask.dependsOn(calculateMetricsTask)
    }
}
