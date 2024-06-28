package com.vk.id.health.metrics.buildspeed

import com.vk.id.health.metrics.storage.FirestoreMetricStorage

internal class BuildSpeedRepository(
    measuredTaskPaths: Set<String>,
) {

    private companion object {
        private const val FIELD_BUILD_DURATION = "BUILD_DURATION_MS"
        private const val FIELD_CONFIGURATION_DURATION = "CONFIGURATION_DURATION_MS"
    }

    private val storage = FirestoreMetricStorage(
        metricName = "Build speed",
        metricCollectionName = "build-speed-metrics",
        diffCollectionName = "build-speed-diffs",
        documentSuffix = measuredTaskPaths.joinToString(),
    )

    internal fun saveBuildDuration(
        buildDuration: Long,
        configurationDuration: Long,
    ) = storage.saveMetrics(
        mapOf(
            FIELD_BUILD_DURATION to buildDuration,
            FIELD_CONFIGURATION_DURATION to configurationDuration,
        )
    )

    internal fun getBuildDuration() = storage.getMetric<Long>(FIELD_BUILD_DURATION)?.takeIf { it != 0L } ?: 1L

    internal fun getConfigurationDuration() = storage.getMetric<Long>(FIELD_CONFIGURATION_DURATION)?.takeIf { it != 0L } ?: 1L

    internal fun saveDiff(diff: String) = storage.saveDiff(diff)

    internal fun getDiff() = storage.getDiff()
}
