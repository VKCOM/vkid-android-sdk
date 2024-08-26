package com.vk.id.health.metrics.buildspeed

import com.vk.id.health.metrics.VKIDHealthMetricsExtension
import com.vk.id.health.metrics.VKIDHeathMetric
import com.vk.id.health.metrics.gitlab.GitlabRepository
import com.vk.id.health.metrics.utils.formatChangePercent
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.invocation.Gradle
import org.gradle.api.provider.Property
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

@Suppress("LongParameterList")
public class VKIDBuildSpeedMetric internal constructor(
    private val title: String?,
    private val isExternal: Boolean,
    private val rootProject: Project,
    private val measuredTaskPaths: Set<String>,
    private val iterations: Int,
    private val warmUps: Int,
    private val cleanAfterEachBuild: Boolean,
) : VKIDHeathMetric {

    private val taskPrefix = "healthMetricsBuildSpeed${measuredTaskPaths.joinToString().sha256()}"
    private val iterationTasks = (1..iterations).map {
        rootProject.tasks.create("${taskPrefix}Iteration$it") {
            measuredTaskPaths.forEach { dependsOn(rootProject.tasks.findByPath(it)) }
        }
    }
    private val warmUpTask = createExecTask("${taskPrefix}WarmUp")
    private val repository = BuildSpeedRepository(measuredTaskPaths = measuredTaskPaths)

    private fun createExecTask(taskName: String) = rootProject.tasks.create(taskName) {
        measuredTaskPaths.forEach { dependsOn(rootProject.tasks.findByPath(it)) }
    }

    override fun exec(project: Project) {
        repository.initMetrics(iterations)
        if (isExternal) return
        repeat(warmUps) { execSingleTask(project, warmUpTask) }
        iterationTasks.forEach { execSingleTask(project, it) }
    }

    private fun execSingleTask(project: Project, task: Task) {
        val mergeRequestId = GitlabRepository.mergeRequestId
        project.exec {
            workingDir = project.projectDir
            @Suppress("SpreadOperator")
            commandLine(
                "./gradlew",
                task.path,
                "--stacktrace",
                "--rerun-tasks",
                "--no-build-cache",
                "-PhealthMetrics.buildSpeed.measure",
                "-PhealthMetrics.common.mergeRequestId=$mergeRequestId",
            )
        }
        if (cleanAfterEachBuild) {
            project.exec {
                workingDir = project.projectDir
                commandLine("./gradlew", "clean", "--stacktrace", "--rerun-tasks", "--no-build-cache")
            }
        }
    }

    override fun getDiff(): String {
        val targetBuildDuration = repository.getTargetBuildDuration(iterations)
        val targetConfigurationDuration = repository.getTargetConfigurationDuration(iterations)
        val sourceBuildDuration = repository.getSourceBuildDuration(iterations)
        val sourceConfigurationDuration = repository.getSourceConfigurationDuration(iterations)
        val durationChangePercent = formatChangePercent(sourceBuildDuration, targetBuildDuration)
        val buildDurationText = formatDurationText(targetBuildDuration)
        val buildDurationChange = "$buildDurationText ($durationChangePercent)"
        val configChangePercent = formatChangePercent(sourceConfigurationDuration, targetConfigurationDuration)
        val configurationDurationText = formatDurationText(targetConfigurationDuration)
        val configDurationChange = "$configurationDurationText ($configChangePercent)"
        val title = title ?: "Build speed report for ${measuredTaskPaths.joinToString()}"
        return """
                |## $title
                || Build                | Configuration         |
                ||----------------------|-----------------------|
                || $buildDurationChange | $configDurationChange |
        """.trimMargin()
    }

    private fun formatDurationText(duration: Long) = duration.toDuration(DurationUnit.MILLISECONDS).absoluteValue.toString()

    internal abstract class BuildDurationService : BuildService<BuildDurationService.Params>, BuildOperationListener {

        interface Params : BuildServiceParameters {
            val measuredTaskPaths: SetProperty<String>
            val iteration: Property<Int>
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
                storage.saveBuildDuration(parameters.iteration.get(), buildDuration, configurationDuration)
            }
        }
    }

    init {
        if (shouldRegisterBuildDurationService()) {
            registerBuildDurationService(rootProject.gradle)
        }
    }

    private fun shouldRegisterBuildDurationService() =
        rootProject.gradle.startParameter.taskNames.let { it.size == 1 && it.first().contains("${taskPrefix}Iteration") } &&
            rootProject.hasProperty("healthMetrics.buildSpeed.measure")

    private fun registerBuildDurationService(gradle: Gradle): Provider<BuildDurationService> {
        val registry = (gradle as DefaultGradle).services[BuildEventListenerRegistryInternal::class.java]
        return gradle.sharedServices.registerIfAbsent("build-duration-service", BuildDurationService::class.java) {
            parameters.measuredTaskPaths.set(measuredTaskPaths)
            parameters.iteration.set(rootProject.gradle.startParameter.taskNames.first().takeLastWhile { it.isDigit() }.toInt())
        }.also(registry::onOperationCompletion)
    }

    public class Builder {

        public var title: String? = null
        public var isExternal: Boolean = false
        public var measuredTaskPaths: Set<String> = emptySet()
        public var iterations: Int = 1
        public var warmUps: Int = 0
        public var cleanAfterEachBuild: Boolean = false
        internal var rootProject: Project? = null

        internal fun build(): VKIDBuildSpeedMetric {
            if (measuredTaskPaths.isEmpty()) {
                error("Tasks for measurement are not specified")
            }
            measuredTaskPaths.filter { !it.startsWith(':') }.takeIf { it.isNotEmpty() }?.joinToString()?.let {
                error("Task path for $it must start with a colon")
            }
            return VKIDBuildSpeedMetric(
                title = title,
                isExternal = isExternal,
                rootProject = checkNotNull(rootProject) { "Project is not specified" },
                measuredTaskPaths = measuredTaskPaths,
                iterations = iterations,
                warmUps = warmUps,
                cleanAfterEachBuild = cleanAfterEachBuild,
            )
        }
    }

    private fun String.sha256() = MessageDigest.getInstance("SHA-256").digest(toByteArray()).fold("") { str, item -> str + "%02x".format(item) }
}
