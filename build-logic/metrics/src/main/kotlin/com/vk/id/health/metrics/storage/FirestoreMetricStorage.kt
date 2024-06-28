package com.vk.id.health.metrics.storage

import com.google.firebase.cloud.FirestoreClient
import com.vk.id.health.metrics.git.Git
import com.vk.id.health.metrics.gitlab.GitlabRepository

internal class FirestoreMetricStorage(
    private val metricName: String,
    private val metricCollectionName: String,
    diffCollectionName: String,
    private val documentSuffix: String,
) {

    private companion object {
        private const val FIELD_DIFF_CONTENT = "DIFF_CONTENT"
    }

    private val diffDocument = FirestoreClient.getFirestore().collection(diffCollectionName)
        .document("${Git.currentCommitHash} $documentSuffix")

    private fun getMetricDocument(commitHash: String) = FirestoreClient.getFirestore()
        .collection(metricCollectionName)
        .document("$commitHash $documentSuffix")

    fun saveMetrics(
        metrics: Map<String, Any>
    ) {
        getMetricDocument(Git.currentCommitHash).set(metrics).get()
    }

    inline fun <reified T> getMetric(
        metricField: String
    ): T? {
        return getMetricDocument(Git.getRootCommitHash(GitlabRepository.sourceBranch, GitlabRepository.targetBranch))
            .get()
            .get()
            .get(metricField, T::class.java)
    }

    fun saveDiff(diff: String) {
        diffDocument.set(mapOf(FIELD_DIFF_CONTENT to diff)).get()
    }

    fun getDiff(): String {
        return (
            diffDocument
                .get()
                .get()
                .get(FIELD_DIFF_CONTENT, String::class.java)
                ?: error("$metricName diff for commit ${Git.currentCommitHash} is not found in Firestore. Doc name should be ${diffDocument.path}")
            )
            .also { diffDocument.delete() }
    }
}
