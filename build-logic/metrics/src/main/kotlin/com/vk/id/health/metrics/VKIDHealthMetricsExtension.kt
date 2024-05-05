package com.vk.id.health.metrics

import com.google.cloud.firestore.Firestore
import org.gradle.api.Project

open class VKIDHealthMetricsExtension {
    internal val stepsInternal = mutableListOf<VKIDHeathMetricsStep>()
    val steps: List<VKIDHeathMetricsStep> get() = stepsInternal.toList()
    internal var rootProjectInternal: Project? = null
    val rootProject get() = checkNotNull(rootProjectInternal) { "Project is not set" }
    internal var firestoreInternal: Firestore? = null
    val firestore get() = checkNotNull(firestoreInternal) { "Firestore is not initialized" }
}