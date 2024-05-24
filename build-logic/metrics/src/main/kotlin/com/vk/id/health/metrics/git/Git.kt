package com.vk.id.health.metrics.git

import java.io.IOException

internal object Git {
    val currentCommitHash get() = exec("git rev-parse --verify HEAD")

    fun getRootCommitHash(sourceBranch: String, targetBranch: String): String {
        val mergeBase = exec("git merge-base $sourceBranch $targetBranch")
        return exec("git rev-list --no-merges -n 1 $mergeBase")
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
