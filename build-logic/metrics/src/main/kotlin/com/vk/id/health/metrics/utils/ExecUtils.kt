package com.vk.id.health.metrics.utils

import java.io.IOException

internal fun exec(command: String): String {
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
