package com.vk.id.health.metrics.publish

import com.vk.id.health.metrics.VKIDHeathMetricsStep
import com.vk.id.health.metrics.gitlab.GitlabRepository
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

internal open class PublishMetricsTask : DefaultTask() {

    @Input
    lateinit var steps: Lazy<List<VKIDHeathMetricsStep>>

    @TaskAction
    fun execute() {
        val diff = steps.value.joinToString("\n") { it.getDiff() }
        runBlocking { GitlabRepository.postCommentToMr(diff) }
    }
}
