package com.vk.id.health.metrics

import org.gradle.api.Task

public interface VKIDHeathMetricsStep {
    public val isExternal: Boolean
    public val task: Task
    public fun getDiff(): String
    public val properties: Array<String>
}
