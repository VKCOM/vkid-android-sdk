package com.vk.id.health.metrics

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.configurationcache.extensions.capitalized

class VKIDBuildSpeedStep(
    rootProject: Project,
    measuredTaskPath: String
) : VKIDHeathMetricsStep {

    private var startTaskTimestamp: Long? = null
    private var endTaskTimestamp: Long? = null
    private val buildDuration
        get() = (endTaskTimestamp ?: error("End time is not initialized"))
            .minus(startTaskTimestamp ?: error("Start time is not initialized"))
    private var durationDiff: Long? = null

    private val measuredTask: Task = rootProject.tasks.getByPath(measuredTaskPath)
    private val taskName = measuredTask.name
    private val publishMetricTask = rootProject.tasks.create("healthMetricsBuildSpeed${taskName.capitalized()}PublishMetric") {
        doLast {
            println("Task :$taskName took $buildDuration milliseconds to execute")
        }
    }
    private val recordStartTimeTask = rootProject.tasks.create("healthMetricsBuildSpeed${taskName.capitalized()}RecordStart") {
        doLast {
            startTaskTimestamp = System.currentTimeMillis()
        }
    }
    private val recordEndTimeTask = rootProject.tasks.create("healthMetricsBuildSpeed${taskName.capitalized()}RecordEnd") {
        doLast {
            endTaskTimestamp = System.currentTimeMillis()
        }
    }
    private val publishDiffTask = rootProject.tasks.create("healthMetricsBuildSpeed${taskName.capitalized()}PublishDiff") {
        doLast {
            val previousBuildDuration = 0L
            durationDiff = buildDuration - previousBuildDuration
            println("Build speed diff is $durationDiff milliseconds")
        }
    }

    override val task: Task = publishDiffTask

    init {
        publishDiffTask.dependsOn(publishMetricTask)
        publishMetricTask.dependsOn(recordEndTimeTask)
        recordEndTimeTask.dependsOn(measuredTask)
        measuredTask.dependsOn(recordStartTimeTask)
    }


    class Builder {

        var rootProject: Project? = null
        var measuredTaskPath: String? = null

        fun build() = VKIDBuildSpeedStep(
            rootProject = checkNotNull(rootProject) { "Project is not specified" },
            measuredTaskPath = checkNotNull(measuredTaskPath) { "Task for measurement is not specified" }
        )
    }
}

fun VKIDHealthMetricsExtension.buildSpeed(configuration: VKIDBuildSpeedStep.Builder.() -> Unit) {
    stepsInternal.add(VKIDBuildSpeedStep.Builder().apply { rootProject = this@buildSpeed.rootProject }.apply(configuration).build())
}
