package com.vk.id.health.metrics.git

import com.vk.id.health.metrics.utils.exec

internal object Git {
    val currentCommitHash get() = exec("git rev-parse --verify HEAD").first()

    fun getRootCommitHash(sourceBranch: String, targetBranch: String): String {
        runCatching { exec("git fetch origin $sourceBranch:$sourceBranch") }
        runCatching { exec("git fetch origin $targetBranch:$targetBranch") }
        val mergeBase = exec("git merge-base $sourceBranch $targetBranch").first()
        return exec("git rev-list --no-merges -n 1 $mergeBase").first()
    }
}
