package com.vk.id

import com.android.build.api.dsl.CommonExtension
import com.vk.id.util.android
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
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
internal open class UninstallTestAppTask : DefaultTask() {

    private val android = project.extensions.android

    @TaskAction
    fun execute() {
        println("adb uninstall ${android.namespace}.test")
        val process = Runtime.getRuntime().exec("adb uninstall ${android.namespace}.test")
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw IOException("Command exited with $exitCode")
        }
    }
}