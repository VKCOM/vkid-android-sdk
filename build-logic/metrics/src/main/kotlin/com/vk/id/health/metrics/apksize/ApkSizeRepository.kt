package com.vk.id.health.metrics.apksize

import com.android.build.gradle.api.ApplicationVariant
import com.vk.id.health.metrics.storage.FirestoreMetricStorage
import org.gradle.api.Project

internal class ApkSizeRepository(
    targetProject: Project,
    targetBuildType: ApplicationVariant,
    sourceProject: Project,
    sourceBuildType: ApplicationVariant?,
) {

    private companion object {
        private const val FIELD_APK_SIZE = "APK_SIZE"
    }

    private val documentSuffix = "${targetProject.name}#${targetBuildType.name} ${sourceProject.name}#${sourceBuildType?.name}"

    private val storage = FirestoreMetricStorage(
        metricName = "Apk size",
        metricCollectionName = "apk-size-metrics",
        diffCollectionName = "apk-size-diffs",
        documentSuffix = documentSuffix,
    )

    internal fun saveApkSize(apkSize: Long) = storage.saveMetrics(mapOf(FIELD_APK_SIZE to apkSize))

    internal fun getApkSize() = storage.getSourceMetric<Long>(FIELD_APK_SIZE)?.takeIf { it != 0L } ?: 1L

    internal fun saveDiff(diff: String) = storage.saveDiff(diff)

    internal fun getDiff() = storage.getDiff()
}
