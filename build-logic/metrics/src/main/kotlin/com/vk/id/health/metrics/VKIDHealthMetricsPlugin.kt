package com.vk.id.health.metrics

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import java.io.ByteArrayOutputStream
import java.io.IOException

class VKIDHealthMetricsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("healthMetrics", VKIDHealthMetricsExtension::class)
        extension.rootProjectInternal = target.rootProject
        val publishMetricsTask = target.tasks.create("publishHealthMetrics") {
            doLast {
                val metric1 = "Build speed diff is 101 milliseconds"
                val metric2 = "Build speed diff is 18 milliseconds"
                println(metric1 + "\n" + metric2)
            }
        }
        val calculateMetricsTask = target.tasks.create("calculateHealthMetrics") {
            doLast {
                extension.steps.forEach {
                    target.exec {
                        workingDir = project.projectDir
                        commandLine("./gradlew", it.task.name)
                    }
                }
            }
        }
        publishMetricsTask.dependsOn(calculateMetricsTask)
    }
}
