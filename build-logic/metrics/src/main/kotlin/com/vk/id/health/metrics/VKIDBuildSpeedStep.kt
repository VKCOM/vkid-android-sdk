package com.vk.id.health.metrics

import org.gradle.api.Task
import org.gradle.configurationcache.extensions.capitalized

class VKIDBuildSpeedStep(
    val measuredTask: Task
) : VKIDHeathMetricsStep {

    private var startTaskTimestamp: Long? = null
    private var endTaskTimestamp: Long? = null

    private val publishDiffTask = measuredTask.project.tasks.create("healthMetricsBuildSpeed${measuredTask.name.capitalized()}PublishDiff") {
        doLast {
            val duration = (endTaskTimestamp ?: error("End time is not initialized"))
                .minus(startTaskTimestamp ?: error("Start time is not initialized"))
            println("Task :${measuredTask.name} took $duration milliseconds to execute")
        }
    }
    private val recordStartTimeTask = measuredTask.project.tasks.create("healthMetricsBuildSpeed${measuredTask.name.capitalized()}RecordStart") {
        doLast {
            startTaskTimestamp = System.currentTimeMillis()
        }
    }
    private val recordEndTimeTask = measuredTask.project.tasks.create("healthMetricsBuildSpeed${measuredTask.name.capitalized()}RecordEnd") {
        doLast {
            endTaskTimestamp = System.currentTimeMillis()
        }
    }

    init {
        publishDiffTask.dependsOn(recordEndTimeTask)
        recordEndTimeTask.dependsOn(measuredTask)
        measuredTask.dependsOn(recordStartTimeTask)
    }

    override val task: Task = publishDiffTask

    class Builder {

        var measuredTask: Task? = null

        fun build() = VKIDBuildSpeedStep(
            measuredTask = measuredTask ?: error("Task for measurement is not specified")
        )
    }
}

fun VKIDHealthMetricsExtension.buildSpeed(configuration: VKIDBuildSpeedStep.Builder.() -> Unit) {
    stepsInternal.add(VKIDBuildSpeedStep.Builder().apply(configuration).build())
}
