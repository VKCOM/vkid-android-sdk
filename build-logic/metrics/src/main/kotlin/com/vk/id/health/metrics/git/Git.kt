package com.vk.id.health.metrics.git

import java.io.IOException

internal object Git {
    val currentCommitHash get() = exec("git rev-parse --verify HEAD")

    val rootCommitHash: String
        get() {
            val sourceBranch = System.getenv("CI_MERGE_REQUEST_SOURCE_BRANCH_NAME") ?: error("Source branch is not specified")
            val targetBranch = System.getenv("CI_MERGE_REQUEST_TARGET_BRANCH_NAME") ?: error("Target branch is not specified")
            return exec("git merge-base $sourceBranch $targetBranch")
        }

    private fun exec(command: String): String {
        val process = Runtime.getRuntime().exec(command)
        val output = StringBuilder()
        process.inputReader().lines().forEach(output::append)
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw IOException("Command exited with $exitCode")
        }
        return output.toString()
    }
}
