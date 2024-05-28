package com.vk.id.health.metrics.buildspeed

import com.google.firebase.cloud.FirestoreClient
import com.vk.id.health.metrics.git.Git
import com.vk.id.health.metrics.gitlab.GitlabRepository

internal class BuildSpeedStorage(
    private val measuredTaskPaths: Set<String>,
) {

    private companion object {
        private const val FIELD_BUILD_DURATION = "BUILD_DURATION_MS"
        private const val FIELD_CONFIGURATION_DURATION = "CONFIGURATION_DURATION_MS"
        private const val FIELD_DIFF_CONTENT = "DIFF_CONTENT"
    }

    private val diffDocument = FirestoreClient.getFirestore().collection("build-speed-diffs")
        .document("${Git.currentCommitHash} ${measuredTaskPaths.joinToString()}")

    internal fun saveBuildDuration(
        buildDuration: Long,
        configurationDuration: Long,
    ) {
        getMetricDocument(Git.currentCommitHash).set(
            mapOf(
                FIELD_BUILD_DURATION to buildDuration,
                FIELD_CONFIGURATION_DURATION to configurationDuration,
            )
        ).get()
    }

    internal fun getBuildDuration(): Long {
        return getMetricDocument(Git.getRootCommitHash(GitlabRepository.sourceBranch, GitlabRepository.targetBranch))
            .get()
            .get()
            .get(FIELD_BUILD_DURATION, Long::class.java)
            ?.takeIf { it != 0L }
            ?: 1L
    }

    internal fun getConfigurationDuration(): Long {
        return getMetricDocument(Git.getRootCommitHash(GitlabRepository.sourceBranch, GitlabRepository.targetBranch))
            .get()
            .get()
            .get(FIELD_CONFIGURATION_DURATION, Long::class.java)
            ?.takeIf { it != 0L }
            ?: 1L
    }

    internal fun saveDiff(diff: String) {
        diffDocument.set(mapOf(FIELD_DIFF_CONTENT to diff)).get()
    }

    internal fun getDiff(): String {
        return (
            diffDocument
                .get()
                .get()
                .get(FIELD_DIFF_CONTENT, String::class.java)
                ?: error("Build speed diff for commit ${Git.currentCommitHash} is not found in Firestore. Doc name should be ${diffDocument.path}")
            )
            .also { diffDocument.delete() }
    }

    private fun getMetricDocument(commitHash: String) = FirestoreClient.getFirestore()
        .collection("build-speed-metrics")
        .document("$commitHash ${measuredTaskPaths.joinToString()}")
}
