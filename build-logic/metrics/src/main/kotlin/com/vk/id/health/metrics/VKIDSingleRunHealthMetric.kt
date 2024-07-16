package com.vk.id.health.metrics

import com.vk.id.health.metrics.gitlab.GitlabRepository
import org.gradle.api.Project
import org.gradle.api.Task

public interface VKIDSingleRunHealthMetric : VKIDHeathMetric {
    public val task: Task
    override fun exec(project: Project) {
        val mergeRequestId = GitlabRepository.mergeRequestId
        project.exec {
            workingDir = project.projectDir
            commandLine(
                "./gradlew",
                task.path,
                "--stacktrace",
                "-PhealthMetrics.common.mergeRequestId=$mergeRequestId",
            )
        }
    }
}
