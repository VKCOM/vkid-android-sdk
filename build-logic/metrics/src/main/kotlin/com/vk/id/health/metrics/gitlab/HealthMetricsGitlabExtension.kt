package com.vk.id.health.metrics.gitlab

import com.vk.id.health.metrics.VKIDHealthMetricsExtension

public fun VKIDHealthMetricsExtension.gitlab(
    host: () -> String = { "https://gitlab.com" },
    token: () -> String,
    projectId: () -> String,
) {
    gitlabHost = lazy(host)
    gitlabToken = lazy(token)
    gitlabProjectId = lazy(projectId)
}
