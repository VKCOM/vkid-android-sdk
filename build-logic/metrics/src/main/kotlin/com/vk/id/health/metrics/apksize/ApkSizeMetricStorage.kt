package com.vk.id.health.metrics.apksize

import com.android.build.gradle.api.ApplicationVariant
import com.google.firebase.cloud.FirestoreClient
import com.vk.id.health.metrics.git.Git
import com.vk.id.health.metrics.gitlab.GitlabRepository
import org.gradle.api.Project

internal class ApkSizeMetricStorage(
    targetProject: Project,
    targetBuildType: ApplicationVariant,
    sourceProject: Project,
    sourceBuildType: ApplicationVariant?,
) {

    private companion object {
        private const val FIELD_DIFF_CONTENT = "DIFF_CONTENT"
        private const val FIELD_APK_SIZE = "APK_SIZE"
    }

    private val documentSuffix = "${targetProject.name}#${targetBuildType.name} ${sourceProject.name}#${sourceBuildType?.name}"
    private val diffDocument = FirestoreClient.getFirestore().collection("apk-size-diffs")
        .document("${Git.currentCommitHash} $documentSuffix")

    internal fun saveApkSize(
        apkSize: Long,
    ) {
        getMetricDocument(Git.currentCommitHash).set(mapOf(FIELD_APK_SIZE to apkSize,)).get()
    }

    internal fun getApkSize(): Long {
        return getMetricDocument(Git.getRootCommitHash(GitlabRepository.sourceBranch, GitlabRepository.targetBranch))
            .get()
            .get()
            .get(FIELD_APK_SIZE, Long::class.java)
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
                ?: error("Apk diff for commit ${Git.currentCommitHash} is not found in Firestore. Doc name should be ${diffDocument.path}")
            )
            .also { diffDocument.delete() }
    }

    private fun getMetricDocument(commitHash: String) = FirestoreClient.getFirestore()
        .collection("apk-size-metrics")
        .document("$commitHash $documentSuffix")
}
