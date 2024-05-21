package com.vk.id.health.metrics.buildspeed

import com.vk.id.health.metrics.VKIDHealthMetricsExtension
import com.vk.id.health.metrics.VKIDHeathMetricsStep
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.invocation.Gradle
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
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
import java.math.BigDecimal
import java.math.RoundingMode

public class VKIDBuildSpeedStep internal constructor(
    override val isExternal: Boolean,
    private val rootProject: Project,
    private val measuredTaskPath: String
) : VKIDHeathMetricsStep {

    private companion object {
        private const val HUNDRED_PERCENT = 100
    }

    override val task: Task = rootProject.tasks.getByPath(measuredTaskPath)
    override val properties: Array<String> = arrayOf("-PhealthMetrics.buildSpeed.measure")

    override fun getDiff(): String = BuildSpeedStorage(measuredTaskPath = measuredTaskPath).getDiff()

    internal abstract class BuildDurationService : BuildService<BuildDurationService.Params>, BuildOperationListener {

        interface Params : BuildServiceParameters {
            val measuredTaskPath: Property<String>
        }

        private val storage = BuildSpeedStorage(measuredTaskPath = parameters.measuredTaskPath.get())

        override fun progress(operationIdentifier: OperationIdentifier, progressEvent: OperationProgressEvent) = Unit

        override fun started(buildOperation: BuildOperationDescriptor, startEvent: OperationStartEvent) = Unit

        override fun finished(buildOperation: BuildOperationDescriptor, finishEvent: OperationFinishEvent) {
            if (buildOperation.details is RunRootBuildWorkBuildOperationType.Details) {
                val details = buildOperation.details as RunRootBuildWorkBuildOperationType.Details?

                details?.buildStartTime?.let { buildStartTime ->
                    val buildDuration = System.currentTimeMillis() - buildStartTime

                    val firstTaskStartTime = finishEvent.startTime
                    val configurationDuration = firstTaskStartTime - buildStartTime
                    storage.saveBuildDuration(buildDuration, configurationDuration)
                    publishDiff(buildDuration, configurationDuration)
                }
            }
        }

        private fun publishDiff(
            buildDuration: Long,
            configurationDuration: Long,
        ) {
            val measuredTaskPath = parameters.measuredTaskPath.get()
            val buildDurationChange = "$buildDuration ms (${formatDurationChangePercent(storage.getBuildDuration(), buildDuration)})"
            val configDurationChange = "$configurationDuration ms (${formatDurationChangePercent(
                storage.getConfigurationDuration(),
                configurationDuration
            )})"
            val diff = """
                |# Build speed report for task $measuredTaskPath
                || Build                | Configuration         |
                ||----------------------|-----------------------|
                || $buildDurationChange | $configDurationChange |
            """.trimMargin()
            storage.saveDiff(diff)
        }

        private fun formatDurationChangePercent(
            oldDuration: Long,
            newDuration: Long,
        ): String {
            val changePercent = BigDecimal(HUNDRED_PERCENT - newDuration.toDouble() / oldDuration * HUNDRED_PERCENT)
                .setScale(2, RoundingMode.HALF_EVEN)
                .stripTrailingZeros()
            val sign = when {
                changePercent > BigDecimal.ZERO -> "-"
                changePercent == BigDecimal.ZERO -> ""
                else -> "+"
            }
            val colorSign = if (sign == "-") "-" else "+"
            return "{$colorSign$sign${changePercent.abs().toPlainString()}%$colorSign}"
        }
    }

    init {
        if (shouldRegisterBuildDurationService()) {
            registerBuildDurationService(rootProject.gradle)
        }
    }

    private fun shouldRegisterBuildDurationService() = rootProject.gradle.startParameter.taskNames == listOf(measuredTaskPath) &&
        rootProject.hasProperty("healthMetrics.buildSpeed.measure")

    private fun registerBuildDurationService(gradle: Gradle): Provider<BuildDurationService> {
        val registry = (gradle as DefaultGradle).services[BuildEventListenerRegistryInternal::class.java]
        return gradle.sharedServices
            .registerIfAbsent("build-duration-service-$measuredTaskPath", BuildDurationService::class.java) {
                parameters.measuredTaskPath.set(measuredTaskPath)
            }
            .also(registry::onOperationCompletion)
    }

    public class Builder {

        public var isExternal: Boolean = false
        public var rootProject: Project? = null
        public var measuredTaskPath: String? = null

        internal fun build(): VKIDBuildSpeedStep {
            val measuredTaskPath = checkNotNull(measuredTaskPath) { "Task for measurement is not specified" }
            return VKIDBuildSpeedStep(
                isExternal = isExternal,
                rootProject = checkNotNull(rootProject) { "Project is not specified" },
                measuredTaskPath = measuredTaskPath
            )
        }
    }
}

public fun VKIDHealthMetricsExtension.buildSpeed(configuration: VKIDBuildSpeedStep.Builder.() -> Unit) {
    stepsInternal.add(VKIDBuildSpeedStep.Builder().apply { rootProject = this@buildSpeed.rootProject }.apply(configuration).build())
}
