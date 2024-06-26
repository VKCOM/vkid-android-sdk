package com.vk.id.health.metrics.buildspeed

import com.vk.id.health.metrics.VKIDHealthMetricsExtension
import com.vk.id.health.metrics.VKIDHeathMetric
import com.vk.id.health.metrics.utils.formatChangePercent
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.invocation.Gradle
import org.gradle.api.provider.Provider
import org.gradle.api.provider.SetProperty
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.execution.RunRootBuildWorkBuildOperationType
import org.gradle.internal.build.event.BuildEventListenerRegistryInternal
import org.gradle.internal.operations.BuildOperationDescriptor
import org.gradle.internal.operations.BuildOperationListener
import org.gradle.internal.operations.OperationFinishEvent
import org.gradle.internal.operations.OperationIdentifier
import org.gradle.internal.operations.OperationProgressEvent
import org.gradle.internal.operations.OperationStartEvent
import org.gradle.invocation.DefaultGradle
import java.security.MessageDigest
import kotlin.time.DurationUnit
import kotlin.time.toDuration

public fun VKIDHealthMetricsExtension.buildSpeed(configuration: VKIDBuildSpeedMetric.Builder.() -> Unit) {
    stepsInternal.add(VKIDBuildSpeedMetric.Builder().apply { rootProject = this@buildSpeed.rootProject }.apply(configuration).build())
}

public class VKIDBuildSpeedMetric internal constructor(
    override val isExternal: Boolean,
    private val rootProject: Project,
    private val measuredTaskPaths: Set<String>
) : VKIDHeathMetric {

    override val task: Task = rootProject.tasks.create("healthMetricsBuildSpeed${measuredTaskPaths.joinToString().sha256()}") {
        measuredTaskPaths.forEach { dependsOn(rootProject.tasks.findByPath(it)) }
    }
    override val properties: Array<String> = arrayOf("-PhealthMetrics.buildSpeed.measure")

    override fun getDiff(): String = BuildSpeedRepository(measuredTaskPaths = measuredTaskPaths).getDiff()

    internal abstract class BuildDurationService : BuildService<BuildDurationService.Params>, BuildOperationListener {

        interface Params : BuildServiceParameters {
            val measuredTaskPaths: SetProperty<String>
        }

        private val storage = BuildSpeedRepository(measuredTaskPaths = parameters.measuredTaskPaths.get())

        override fun progress(operationIdentifier: OperationIdentifier, progressEvent: OperationProgressEvent) = Unit

        override fun started(buildOperation: BuildOperationDescriptor, startEvent: OperationStartEvent) = Unit

        override fun finished(buildOperation: BuildOperationDescriptor, finishEvent: OperationFinishEvent) {
            val details = buildOperation.details as? RunRootBuildWorkBuildOperationType.Details
            if (details != null) {
                val buildDuration = System.currentTimeMillis() - details.buildStartTime

                val firstTaskStartTime = finishEvent.startTime
                val configurationDuration = firstTaskStartTime - details.buildStartTime
                storage.saveBuildDuration(buildDuration, configurationDuration)
                publishDiff(buildDuration, configurationDuration)
            }
        }

        private fun publishDiff(
            buildDuration: Long,
            configurationDuration: Long,
        ) {
            val measuredTaskPath = parameters.measuredTaskPaths.get()
            val durationChangePercent = formatChangePercent(storage.getBuildDuration(), buildDuration)
            val buildDurationText = formatDurationText(buildDuration)
            val buildDurationChange = "$buildDurationText ($durationChangePercent)"
            val configChangePercent = formatChangePercent(storage.getConfigurationDuration(), configurationDuration)
            val configurationDurationText = formatDurationText(configurationDuration)
            val configDurationChange = "$configurationDurationText ($configChangePercent)"
            val diff = """
                |# Build speed report for $measuredTaskPath
                || Build                | Configuration         |
                ||----------------------|-----------------------|
                || $buildDurationChange | $configDurationChange |
            """.trimMargin()
            storage.saveDiff(diff)
        }

        private fun formatDurationText(duration: Long) = duration.toDuration(DurationUnit.MILLISECONDS).absoluteValue.toString()
    }

    init {
        if (shouldRegisterBuildDurationService()) {
            registerBuildDurationService(rootProject.gradle)
        }
    }

    private fun shouldRegisterBuildDurationService() = rootProject.gradle.startParameter.taskNames == listOf(task.path) &&
        rootProject.hasProperty("healthMetrics.buildSpeed.measure")

    private fun registerBuildDurationService(gradle: Gradle): Provider<BuildDurationService> {
        val registry = (gradle as DefaultGradle).services[BuildEventListenerRegistryInternal::class.java]
        return gradle.sharedServices
            .registerIfAbsent("build-duration-service", BuildDurationService::class.java) {
                parameters.measuredTaskPaths.set(measuredTaskPaths)
            }
            .also(registry::onOperationCompletion)
    }

    public class Builder {

        public var isExternal: Boolean = false
        public var rootProject: Project? = null
        public var measuredTaskPaths: Set<String> = emptySet()

        internal fun build(): VKIDBuildSpeedMetric {
            if (measuredTaskPaths.isEmpty()) {
                error("Tasks for measurement are not specified")
            }
            measuredTaskPaths.filter { !it.startsWith(':') }.takeIf { it.isNotEmpty() }?.joinToString()?.let {
                error("Task path for $it must start with a colon")
            }
            return VKIDBuildSpeedMetric(
                isExternal = isExternal,
                rootProject = checkNotNull(rootProject) { "Project is not specified" },
                measuredTaskPaths = measuredTaskPaths
            )
        }
    }

    private fun String.sha256() = MessageDigest.getInstance("SHA-256")
        .digest(toByteArray())
        .fold("") { str, item -> str + "%02x".format(item) }
}
