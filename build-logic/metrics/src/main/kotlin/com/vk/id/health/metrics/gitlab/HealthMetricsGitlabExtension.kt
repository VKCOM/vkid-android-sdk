package com.vk.id.health.metrics.gitlab

import com.vk.id.health.metrics.VKIDHealthMetricsExtension
import java.util.Properties

public fun VKIDHealthMetricsExtension.gitlab() {
    gitlabToken = lazy {
        val properties = Properties()
        properties.load(rootProject.file("local.properties").inputStream())
        properties.getProperty("healthmetrics.gitlab.token")
    }
}
