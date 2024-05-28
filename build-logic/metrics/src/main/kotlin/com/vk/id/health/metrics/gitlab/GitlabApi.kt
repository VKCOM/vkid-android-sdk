package com.vk.id.health.metrics.gitlab

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface GitlabApi {

    @POST("projects/{project_id}/merge_requests/{merge_request_id}/notes")
    suspend fun postCommentToMr(
        @Path("project_id") projectId: String,
        @Path("merge_request_id") mergeRequestId: String,
        @Body body: GitlabPostCommentToMrBody,
    )

    @GET("projects/{project_id}/merge_requests/{merge_request_id}")
    suspend fun getMergeRequest(
        @Path("project_id") projectId: String,
        @Path("merge_request_id") mergeRequestId: String,
    ): GitlabMergeRequestResponse
}
