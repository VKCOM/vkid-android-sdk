package com.vk.id.health.metrics

import org.gradle.api.Project

public interface VKIDHeathMetric {
    public fun getDiff(): String
    public fun exec(project: Project)
}
