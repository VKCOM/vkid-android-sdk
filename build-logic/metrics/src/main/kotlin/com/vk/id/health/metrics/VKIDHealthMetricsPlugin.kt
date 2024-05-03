package com.vk.id.health.metrics

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class VKIDHealthMetricsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("healthMetrics", VKIDHealthMetricsExtension::class)
        target.tasks.create("publishHealthMetrics") {
            doLast {
                extension.steps.forEach {
                    target.exec {
                        workingDir = project.projectDir
                        commandLine("./gradlew", it.task.name)
                    }
                }
            }
        }
    }
}
