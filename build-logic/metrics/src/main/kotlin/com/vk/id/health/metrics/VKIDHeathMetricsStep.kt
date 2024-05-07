package com.vk.id.health.metrics

import org.gradle.api.Task

interface VKIDHeathMetricsStep {
    val task: Task
    fun getDiff(): String
}