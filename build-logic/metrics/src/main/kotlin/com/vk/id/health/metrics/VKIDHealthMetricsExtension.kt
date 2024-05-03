package com.vk.id.health.metrics

open class VKIDHealthMetricsExtension {
    internal val stepsInternal = mutableListOf<VKIDHeathMetricsStep>()
    val steps: List<VKIDHeathMetricsStep> get() = stepsInternal.toList()
}