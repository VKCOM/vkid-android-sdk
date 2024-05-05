package com.vk.id.health.metrics.buildspeed

import com.google.cloud.firestore.Firestore
import com.vk.id.health.metrics.VKIDHealthMetricsExtension
import com.vk.id.health.metrics.VKIDHeathMetricsStep
import com.vk.id.health.metrics.git.Git
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.configurationcache.extensions.capitalized
import java.math.BigDecimal
import java.math.RoundingMode

class VKIDBuildSpeedStep(
    private val storage: BuildSpeedStorage,
    rootProject: Project,
    measuredTaskPath: String
) : VKIDHeathMetricsStep {

    private var startTaskTimestamp: Long? = null
    private var endTaskTimestamp: Long? = null
    private val buildDuration
        get() = (endTaskTimestamp ?: error("End time is not initialized"))
            .minus(startTaskTimestamp ?: error("Start time is not initialized"))

    private val measuredTask: Task = rootProject.tasks.getByPath(measuredTaskPath)
    private val projectName = measuredTask.project.name
    private val taskName = measuredTask.name
    private val taskNameAppendix = projectName.capitalized() + taskName.capitalized()
    private val publishMetricTask = rootProject.tasks.create("healthMetricsBuildSpeed${taskNameAppendix}PublishMetric") {
        doLast {
            storage.saveBuildSpeed(buildDuration)
        }
    }
    private val recordStartTimeTask = rootProject.tasks.create("healthMetricsBuildSpeed${taskNameAppendix}RecordStart") {
        doLast {
            startTaskTimestamp = System.currentTimeMillis()
        }
    }
    private val recordEndTimeTask = rootProject.tasks.create("healthMetricsBuildSpeed${taskNameAppendix}RecordEnd") {
        doLast {
            endTaskTimestamp = System.currentTimeMillis()
        }
    }
    private val publishDiffTask = rootProject.tasks.create("healthMetricsBuildSpeed${taskNameAppendix}PublishDiff") {
        doLast {
            val previousBuildDuration = storage.getBuildSpeed()
            val changePercent = BigDecimal(100 - buildDuration.toDouble() / previousBuildDuration * 100)
                .setScale(2, RoundingMode.HALF_EVEN)
            val sign = when {
                changePercent > BigDecimal.ZERO -> "-"
                changePercent == BigDecimal.ZERO -> ""
                else -> "+"
            }
            val diff = """
                |The task $measuredTaskPath was executing for $buildDuration ms
                |Change: $sign${changePercent.abs()}%
            """.trimMargin()
            storage.saveDiff(diff)
        }
    }

    override val task = publishDiffTask

    override fun getDiff() = storage.getDiff()

    init {
        publishDiffTask.dependsOn(publishMetricTask)
        publishMetricTask.dependsOn(recordEndTimeTask)
        recordEndTimeTask.dependsOn(measuredTask)
        measuredTask.dependsOn(recordStartTimeTask)
    }


    class Builder(
        private val firestore: Firestore
    ) {

        var rootProject: Project? = null
        var measuredTaskPath: String? = null

        fun build(): VKIDBuildSpeedStep {
            val measuredTaskPath = checkNotNull(measuredTaskPath) { "Task for measurement is not specified" }
            return VKIDBuildSpeedStep(
                storage = BuildSpeedStorage(firestore, measuredTaskPath),
                rootProject = checkNotNull(rootProject) { "Project is not specified" },
                measuredTaskPath = measuredTaskPath
            )
        }
    }
}

fun VKIDHealthMetricsExtension.buildSpeed(configuration: VKIDBuildSpeedStep.Builder.() -> Unit) {
    stepsInternal.add(VKIDBuildSpeedStep.Builder(firestore).apply { rootProject = this@buildSpeed.rootProject }.apply(configuration).build())
}
