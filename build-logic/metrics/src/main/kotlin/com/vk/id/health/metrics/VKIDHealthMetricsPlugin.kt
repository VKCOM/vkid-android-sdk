package com.vk.id.health.metrics

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

internal class VKIDHealthMetricsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("healthMetrics", VKIDHealthMetricsExtension::class)
        extension.rootProjectInternal = target.rootProject
        val publishMetricsTask = target.tasks.create("publishHealthMetrics") {
            doLast {
                println(extension.steps.joinToString("\n") { it.getDiff() })
            }
        }
        val sourceBranch = target.properties["healthMetrics.common.sourceBranch"] as String?
        val targetBranch = target.properties["healthMetrics.common.targetBranch"] as String?
        val calculateMetricsTask = target.tasks.create("calculateHealthMetrics") {
            doLast {
                extension.steps.filterNot { it.isExternal }.forEach {
                    target.exec {
                        workingDir = project.projectDir
                        @Suppress("SpreadOperator")
                        commandLine(
                            "./gradlew",
                            it.task.path,
                            "--stacktrace",
                            *(it.properties),
                            "-PhealthMetrics.common.sourceBranch=$sourceBranch",
                            "-PhealthMetrics.common.targetBranch=$targetBranch"
                        )
                    }
                }
            }
        }
        publishMetricsTask.dependsOn(calculateMetricsTask)
    }
}
