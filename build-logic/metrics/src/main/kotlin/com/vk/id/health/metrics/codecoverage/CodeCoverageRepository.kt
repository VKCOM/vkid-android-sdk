package com.vk.id.health.metrics.codecoverage

import com.vk.id.health.metrics.storage.FirestoreMetricStorage
import org.gradle.api.Project

internal class CodeCoverageRepository(
    targetProject: Project,
) {

    private companion object {
        private const val FIELD_CODE_COVERAGE = "CODE_COVERAGE"
    }

    private val documentSuffix = targetProject.name

    private val storage = FirestoreMetricStorage(
        metricName = "Code coverage",
        metricCollectionName = "code-coverage-metrics",
        diffCollectionName = "code-coverage-diffs",
        documentSuffix = documentSuffix,
    )

    internal fun saveCodeCoverage(coverage: Double) = storage.saveMetrics(mapOf(FIELD_CODE_COVERAGE to coverage))

    internal fun getCodeCoverage() = storage.getSourceMetric<Double>(FIELD_CODE_COVERAGE) ?: 0.0

    internal fun saveDiff(diff: String) = storage.saveDiff(diff)

    internal fun getDiff() = storage.getDiff()
}
