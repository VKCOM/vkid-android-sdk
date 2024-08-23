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
        (target.properties["healthMetrics.common.mergeRequestId"] as String?)?.let {
            GitlabRepository.init(
                host = lazy { extension.gitlabHost.value },
                token = lazy { extension.gitlabToken.value },
                projectId = lazy { extension.gitlabProjectId.value },
                mergeRequestId = it,
            )
        }
        val publishMetricsTask = target.tasks.create("publishHealthMetrics", PublishMetricsTask::class.java) {
            steps = lazy { extension.steps }
        }
        val calculateMetricsTask = target.tasks.create("calculateHealthMetrics") {
            doLast { extension.steps.forEach { it.exec(target) } }
        }
        publishMetricsTask.dependsOn(calculateMetricsTask)
    }
}
