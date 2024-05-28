package com.vk.id.health.metrics.gitlab

import com.vk.id.health.metrics.VKIDHealthMetricsExtension
import java.util.Properties

public fun VKIDHealthMetricsExtension.gitlab() {
    val properties = Properties()
    properties.load(rootProject.file("local.properties").inputStream())
    gitlabToken = properties.getProperty("healthmetrics.gitlab.token")
}
