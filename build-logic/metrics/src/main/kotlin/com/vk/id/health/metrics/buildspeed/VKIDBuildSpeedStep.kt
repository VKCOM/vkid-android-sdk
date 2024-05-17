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

    private val sourceBranch
        get() = rootProject.properties["healthMetrics.common.sourceBranch"] as String? ?: error("Source branch is not specified")
    private val targetBranch
        get() = rootProject.properties["healthMetrics.common.targetBranch"] as String? ?: error("Target branch is not specified")

    override val task: Task = rootProject.tasks.getByPath(measuredTaskPath)
    override val properties: Array<String> = arrayOf("-PhealthMetrics.buildSpeed.measure")

    override fun getDiff(): String = BuildSpeedStorage(
        measuredTaskPath = measuredTaskPath,
        sourceBranch = sourceBranch,
        targetBranch = targetBranch,
    ).getDiff()

    internal abstract class BuildDurationService : BuildService<BuildDurationService.Params>, BuildOperationListener {

        interface Params : BuildServiceParameters {
            val measuredTaskPath: Property<String>
            val sourceBranch: Property<String>
            val targetBranch: Property<String>
        }

        private val storage = BuildSpeedStorage(
            measuredTaskPath = parameters.measuredTaskPath.get(),
            sourceBranch = parameters.sourceBranch.get(),
            targetBranch = parameters.targetBranch.get(),
        )

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
            val diff = """
                |The task $measuredTaskPath was executing for $buildDuration ms
                |Change: ${formatDurationChangePercent(storage.getBuildDuration(), buildDuration)}
                |Configuration took $configurationDuration ms
                |Change: ${formatDurationChangePercent(storage.getConfigurationDuration(), configurationDuration)}
            """.trimMargin()
            storage.saveDiff(diff)
        }

        private fun formatDurationChangePercent(
            oldDuration: Long,
            newDuration: Long,
        ): String {
            val changePercent = BigDecimal(HUNDRED_PERCENT - newDuration.toDouble() / oldDuration * HUNDRED_PERCENT)
                .setScale(2, RoundingMode.HALF_EVEN)
            val sign = when {
                changePercent > BigDecimal.ZERO -> "-"
                changePercent == BigDecimal.ZERO -> ""
                else -> "+"
            }
            return "$sign${changePercent.abs()}%"
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
                parameters.sourceBranch.set(sourceBranch)
                parameters.targetBranch.set(targetBranch)
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
