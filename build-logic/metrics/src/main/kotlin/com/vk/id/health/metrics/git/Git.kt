package com.vk.id.health.metrics.git

import java.io.IOException

internal object Git {
    val currentCommitHash get() = exec("git rev-parse --verify HEAD")

    fun getRootCommitHash(sourceBranch: String, targetBranch: String): String {
        runCatching { exec("git fetch origin $sourceBranch:$sourceBranch") }
        runCatching { exec("git fetch origin $targetBranch:$targetBranch") }
        val mergeBase = exec("git merge-base $sourceBranch $targetBranch")
        return exec("git rev-list --no-merges -n 1 $mergeBase")
    }

    private fun exec(command: String): String {
        val process = Runtime.getRuntime().exec(command)
        val output = StringBuilder()
        val error = StringBuilder()
        process.inputReader().lines().forEach(output::append)
        process.errorReader().lines().forEach { error.append("$it\n") }
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw IOException("Command exited with $exitCode\n$error")
        }
        return output.toString()
    }
}
