package com.vk.id.health.metrics

import org.gradle.api.Project

open class VKIDHealthMetricsExtension {
    internal val stepsInternal = mutableListOf<VKIDHeathMetricsStep>()
    val steps: List<VKIDHeathMetricsStep> get() = stepsInternal.toList()
    internal var rootProjectInternal: Project? = null
    val rootProject get() = checkNotNull(rootProjectInternal) { "Project is not set" }
}