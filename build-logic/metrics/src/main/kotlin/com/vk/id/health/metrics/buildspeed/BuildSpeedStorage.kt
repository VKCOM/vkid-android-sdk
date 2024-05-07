package com.vk.id.health.metrics.buildspeed

import com.google.cloud.firestore.Firestore
import com.vk.id.health.metrics.git.Git

class BuildSpeedStorage(
    private val firestore: Firestore,
    private val measuredTaskPath: String,
) {

    companion object {
        private const val FIELD_BUILD_DURATION = "BUILD_DURATION_MS"
        private const val FIELD_DIFF_CONTENT = "DIFF_CONTENT"
    }

    private val diffDocument = firestore.collection("build-speed-diffs")
        .document("${Git.currentCommitHash} $measuredTaskPath")

    fun saveBuildSpeed(buildDuration: Long) {
        getMetricDocument(Git.currentCommitHash).set(mapOf(FIELD_BUILD_DURATION to buildDuration)).get()
    }

    fun getBuildSpeed(): Long {
        return getMetricDocument(Git.rootCommitHash)
            .get()
            .get()
            .get(FIELD_BUILD_DURATION, Long::class.java)
            ?.takeIf { it != 0L }
            ?: 1L
    }

    fun saveDiff(diff: String) {
        diffDocument.set(mapOf(FIELD_DIFF_CONTENT to diff)).get()
    }

    fun getDiff(): String {
        return (diffDocument
            .get()
            .get()
            .get(FIELD_DIFF_CONTENT, String::class.java)
            ?: error("Build speed diff for commit ${Git.currentCommitHash} is not found in Firestore"))
            .also { diffDocument.delete() }
    }

    private fun getMetricDocument(commitHash: String) = firestore
        .collection("build-speed-metrics")
        .document("$commitHash $measuredTaskPath")
}