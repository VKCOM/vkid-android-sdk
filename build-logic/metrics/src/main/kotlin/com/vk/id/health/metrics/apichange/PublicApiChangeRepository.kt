package com.vk.id.health.metrics.apichange

import com.vk.id.health.metrics.storage.FirestoreMetricStorage

internal class PublicApiChangeRepository {

    private val storage = FirestoreMetricStorage(
        metricName = "Public api change",
        metricCollectionName = "public-api-change-metrics",
        diffCollectionName = "public-api-change-diffs",
        documentSuffix = "",
    )

    internal fun saveDiff(diff: String) = storage.saveDiff(diff)

    internal fun getDiff() = storage.getDiff()
}
