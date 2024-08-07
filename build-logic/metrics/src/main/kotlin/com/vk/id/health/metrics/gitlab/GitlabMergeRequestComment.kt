package com.vk.id.health.metrics.gitlab

internal data class GitlabMergeRequestComment(
    val id: Long,
    val system: Boolean,
    val author: GitlabCommentAuthor
)
