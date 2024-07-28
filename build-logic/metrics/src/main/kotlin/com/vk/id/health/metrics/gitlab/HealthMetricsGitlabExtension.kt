package com.vk.id.health.metrics.gitlab

import com.vk.id.health.metrics.VKIDHealthMetricsExtension

public fun VKIDHealthMetricsExtension.gitlab(
    tokenProvider: () -> String
) {
    gitlabToken = lazy(tokenProvider)
}
