package com.vk.id.health.metrics.gitlab

import com.google.gson.annotations.SerializedName

internal class GitlabMergeRequestResponse(
    @SerializedName("source_branch")
    val sourceBranch: String,
    @SerializedName("target_branch")
    val targetBranch: String,
)
