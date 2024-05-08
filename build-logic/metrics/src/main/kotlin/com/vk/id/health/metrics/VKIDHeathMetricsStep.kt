package com.vk.id.health.metrics

import org.gradle.api.Task

public interface VKIDHeathMetricsStep {
    public val task: Task
    public fun getDiff(): String
}
