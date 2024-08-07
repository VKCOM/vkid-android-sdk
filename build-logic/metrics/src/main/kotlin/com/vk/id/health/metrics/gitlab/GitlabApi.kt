package com.vk.id.health.metrics.gitlab

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

internal interface GitlabApi {

    @GET("user")
    suspend fun getUser(): GitlabUserResponse

    @GET("projects/{project_id}/merge_requests/{merge_request_id}/notes")
    suspend fun listComments(
        @Path("project_id") projectId: String,
        @Path("merge_request_id") mergeRequestId: String,
    ): List<GitlabMergeRequestComment>

    @PUT("projects/{project_id}/merge_requests/{merge_request_id}/notes/{note_id}")
    suspend fun updateComment(
        @Path("project_id") projectId: String,
        @Path("merge_request_id") mergeRequestId: String,
        @Path("note_id") commentId: String,
        @Query("body") comment: String,
    )

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
