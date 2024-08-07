package com.vk.id.health.metrics.buildspeed

import com.vk.id.health.metrics.storage.FirestoreMetricStorage

internal class BuildSpeedRepository(
    measuredTaskPaths: Set<String>,
) {

    private val storage = FirestoreMetricStorage(
        metricName = "Build speed",
        metricCollectionName = "build-speed-metrics",
        diffCollectionName = "build-speed-diffs",
        documentSuffix = measuredTaskPaths.joinToString(),
    )

    fun initMetrics(iterations: Int) {
        (1..iterations)
            .flatMap { listOf(getBuildDurationField(it) to 0, getConfigurationDurationField(it) to 0) }
            .toMap()
            .let(storage::saveMetrics)
    }

    internal fun saveBuildDuration(
        iteration: Int,
        buildDuration: Long,
        configurationDuration: Long,
    ) = storage.updateMetrics(
        mapOf(
            getBuildDurationField(iteration) to buildDuration,
            getConfigurationDurationField(iteration) to configurationDuration,
        )
    )

    internal fun getSourceBuildDuration(iterations: Int): Long {
        return getBuildDuration(iterations, ::getBuildDurationField) { storage.getSourceMetric<Long>(it) }
    }

    internal fun getTargetBuildDuration(iterations: Int): Long {
        return getBuildDuration(iterations, ::getBuildDurationField) { storage.getTargetMetric<Long>(it) }
    }

    internal fun getSourceConfigurationDuration(iterations: Int): Long {
        return getBuildDuration(iterations, ::getConfigurationDurationField) { storage.getSourceMetric<Long>(it) }
    }

    internal fun getTargetConfigurationDuration(iterations: Int): Long {
        return getBuildDuration(iterations, ::getConfigurationDurationField) { storage.getTargetMetric<Long>(it) }
    }

    private fun getBuildDuration(
        iterations: Int,
        fieldAccessor: (iteration: Int) -> String,
        metricAccessor: (field: String) -> Long?
    ): Long {
        return (1..iterations)
            .map { fieldAccessor(it) }
            .sumOf { metricAccessor(it) ?: 0 }
            .div(iterations)
    }

    private fun getBuildDurationField(iteration: Int) = "BUILD_DURATION_MS_$iteration"
    private fun getConfigurationDurationField(iteration: Int) = "CONFIGURATION_DURATION_MS_$iteration"
}
