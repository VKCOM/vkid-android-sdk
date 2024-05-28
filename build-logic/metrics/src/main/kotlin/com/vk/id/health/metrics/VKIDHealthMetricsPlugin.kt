package com.vk.id.health.metrics

import com.vk.id.health.metrics.gitlab.GitlabRepository
import com.vk.id.health.metrics.publish.PublishMetricsTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

internal class VKIDHealthMetricsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("healthMetrics", VKIDHealthMetricsExtension::class)
        extension.rootProjectInternal = target.rootProject
        val mergeRequestId = target.properties["healthMetrics.common.mergeRequestId"] as String?
        mergeRequestId?.let {
            GitlabRepository.init(
                token = lazy { extension.gitlabToken },
                mergeRequestId = it
            )
        }
        val publishMetricsTask = target.tasks.create("publishHealthMetrics", PublishMetricsTask::class.java) {
            steps = lazy { extension.steps }
        }
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
                            "-PhealthMetrics.common.mergeRequestId=$mergeRequestId",
                        )
                    }
                }
            }
        }
        publishMetricsTask.dependsOn(calculateMetricsTask)
    }
}
