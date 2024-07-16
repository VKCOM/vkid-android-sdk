package com.vk.id.health.metrics.apichange

import com.vk.id.health.metrics.VKIDHealthMetricsExtension
import com.vk.id.health.metrics.VKIDSingleRunHealthMetric
import com.vk.id.health.metrics.git.Git
import com.vk.id.health.metrics.gitlab.GitlabRepository
import com.vk.id.health.metrics.utils.exec
import org.gradle.api.Project
import org.gradle.api.Task

public fun VKIDHealthMetricsExtension.publicApiChanges(configuration: VKIDPublicApiChangesMetric.Builder.() -> Unit = {}) {
    stepsInternal.add(VKIDPublicApiChangesMetric.Builder().apply { rootProject = this@publicApiChanges.rootProject }.apply(configuration).build())
}

public class VKIDPublicApiChangesMetric(
    rootProject: Project
) : VKIDSingleRunHealthMetric {

    private val repository = PublicApiChangeRepository()

    override val task: Task = rootProject.tasks.create("healthMetricsPublicApiChange") {
        doLast {
            val rootCommit = Git.getRootCommitHash(GitlabRepository.sourceBranch, GitlabRepository.targetBranch)
            val changedLines = exec("git reset $rootCommit")
            val hasChanges = changedLines.any { it.endsWith(".api") }
            exec("git reset HEAD@{1}")
            val text = if (hasChanges) "There are public api changes, please review them" else "Public api wasn't changed"
            val diff = """
                |# Public api change
                |$text
            """.trimMargin()
            repository.saveDiff(diff)
        }
    }

    override fun getDiff(): String = repository.getDiff()

    public class Builder {

        internal var rootProject: Project? = null

        internal fun build(): VKIDPublicApiChangesMetric {
            return VKIDPublicApiChangesMetric(
                rootProject = checkNotNull(rootProject) { "Project is not specified" },
            )
        }
    }
}
