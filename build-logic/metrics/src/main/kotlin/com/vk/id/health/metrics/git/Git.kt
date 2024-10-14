package com.vk.id.health.metrics.git

import com.vk.id.health.metrics.utils.execute

internal object Git {
    val currentCommitHash get() = execute("git rev-parse --verify HEAD").first()

    fun getRootCommitHash(sourceBranch: String, targetBranch: String): String {
        executeGitFetch(sourceBranch)
        executeGitFetch(targetBranch)
        val mergeBase = execute("git merge-base $sourceBranch $targetBranch").first()
        return execute("git rev-list --no-merges -n 1 $mergeBase").first()
    }

    private fun executeGitFetch(branch: String) {
        try {
            execute("git fetch origin $branch:$branch")
        } catch (@Suppress("TooGenericExceptionCaught") t: Throwable) {
            if (
                t.message?.contains("refusing to fetch into branch") == false &&
                t.message?.contains("[rejected]") == false
            ) {
                throw t
            }
        }
    }
}
