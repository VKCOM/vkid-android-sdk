package com.vk.id.health.metrics.storage

import com.vk.id.health.metrics.git.Git
import com.vk.id.health.metrics.gitlab.GitlabRepository

internal class FirestoreMetricStorage(
    private val metricName: String,
    private val metricCollectionName: String,
    private val diffCollectionName: String,
    private val documentSuffix: String,
) {

    private companion object {
        private const val FIELD_DIFF_CONTENT = "DIFF_CONTENT"
    }

    private fun getDiffDocument() = FirestoreHolder.instance.collection(diffCollectionName)
        .document("${Git.currentCommitHash} $documentSuffix")

    private fun getMetricDocument(commitHash: String) = FirestoreHolder.instance
        .collection(metricCollectionName)
        .document("$commitHash $documentSuffix")

    fun updateMetrics(
        metrics: Map<String, Any>
    ) {
        getMetricDocument(Git.currentCommitHash).update(metrics).get()
    }

    fun saveMetrics(
        metrics: Map<String, Any>
    ) {
        getMetricDocument(Git.currentCommitHash).set(metrics).get()
    }

    inline fun <reified T> getTargetMetric(
        metricField: String
    ): T? {
        return getMetric(Git.currentCommitHash, metricField)
    }

    inline fun <reified T> getSourceMetric(
        metricField: String
    ): T? {
        return getMetric(Git.getRootCommitHash(GitlabRepository.sourceBranch, GitlabRepository.targetBranch), metricField)
    }

    private inline fun <reified T> getMetric(
        commitHash: String,
        metricField: String
    ): T? {
        return getMetricDocument(commitHash).get().get().get(metricField, T::class.java)
    }

    fun saveDiff(diff: String) {
        getDiffDocument().set(mapOf(FIELD_DIFF_CONTENT to diff)).get()
    }

    fun getDiff(): String {
        val commitHash = Git.currentCommitHash
        return (
            getDiffDocument()
                .get()
                .get()
                .get(FIELD_DIFF_CONTENT, String::class.java)
                ?: error("$metricName diff for commit $commitHash isn't found in Firestore. Doc name should be ${getDiffDocument().path}")
            )
            .also { getDiffDocument().delete() }
    }
}
