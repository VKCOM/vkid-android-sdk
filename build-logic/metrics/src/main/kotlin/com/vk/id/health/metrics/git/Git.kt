package com.vk.id.health.metrics.git

import com.vk.id.health.metrics.utils.execute

internal object Git {
    val currentCommitHash get() = execute("git rev-parse --verify HEAD").first()

    fun getRootCommitHash(sourceBranch: String, targetBranch: String): String {
        runCatching { execute("git fetch origin $sourceBranch:$sourceBranch") }
        runCatching { execute("git fetch origin $targetBranch:$targetBranch") }
        val mergeBase = execute("git merge-base $sourceBranch $targetBranch").first()
        return execute("git rev-list --no-merges -n 1 $mergeBase").first()
    }
}
