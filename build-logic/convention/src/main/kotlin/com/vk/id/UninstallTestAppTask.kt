package com.vk.id

import java.io.IOException


/**
 * Uninstalls test app after running tests on it.
 *
 * This prevents collisions when running tests on multiple modules.
 * In this case multiple test apps are installed simultaneously.
 * For example, all of them may handle the same intent which will cause chooser dialog to appear and interfere with running tests.
 *
 * Build system doesn't uninstall apks to save time, but this prevents clean test running.
 */
fun uninstallTestAppTask(namespace: String) {
    println("adb uninstall $namespace.test")
    val process = Runtime.getRuntime().exec("adb uninstall $namespace.test")
    process.errorReader().lines().forEach(::println)
    val exitCode = process.waitFor()
    if (exitCode != 0) {
        throw IOException("Command exited with $exitCode")
    }
}