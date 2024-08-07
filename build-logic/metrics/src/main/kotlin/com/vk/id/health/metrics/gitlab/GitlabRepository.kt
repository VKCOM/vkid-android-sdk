package com.vk.id.health.metrics.gitlab

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

internal object GitlabRepository {
    private const val PROJECT_ID = "2796"
    private lateinit var host: Lazy<String>
    private lateinit var token: Lazy<String>
    private val api by lazy { GitlabApiService(host.value, token.value) }
    private lateinit var mergeRequestIdInternal: String
    private val mergeRequest by lazy { runBlocking { getMergeRequestBranches() } }
    val sourceBranch get() = mergeRequest.sourceBranch
    val targetBranch get() = mergeRequest.targetBranch
    val mergeRequestId get() = mergeRequestIdInternal

    fun init(
        host: Lazy<String>,
        token: Lazy<String>,
        mergeRequestId: String
    ) {
        this.host = host
        this.token = token
        this.mergeRequestIdInternal = mergeRequestId
    }

    suspend fun postCommentToMr(comment: String) {
        withContext(Dispatchers.IO) {
            val username = api.getUser().username
            val comments = api.listComments(PROJECT_ID, mergeRequestId)
            comments.lastOrNull { !it.system && it.author.username == username }
                ?.let {
                    api.updateComment(
                        projectId = PROJECT_ID,
                        mergeRequestId = mergeRequestId,
                        commentId = it.id.toString(),
                        comment = comment,
                    )
                }
                ?: api.postCommentToMr(
                    projectId = PROJECT_ID,
                    mergeRequestId = mergeRequestId,
                    body = GitlabPostCommentToMrBody(body = comment),
                )
        }
    }

    private suspend fun getMergeRequestBranches(): GitlabMergeRequestResponse {
        return withContext(Dispatchers.IO) {
            api.getMergeRequest(
                projectId = PROJECT_ID,
                mergeRequestId = mergeRequestId,
            )
        }
    }
}
