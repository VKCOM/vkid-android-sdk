package com.vk.id.health.metrics

import org.gradle.api.Project
import java.io.File

public open class VKIDHealthMetricsExtension {
    internal val stepsInternal = mutableListOf<VKIDHeathMetricsStep>()
    public val steps: List<VKIDHeathMetricsStep> get() = stepsInternal.toList()
    internal var rootProjectInternal: Project? = null
    public val rootProject: Project get() = checkNotNull(rootProjectInternal) { "Project is not set" }
    internal var firestoreAccountFileInternal: File? = null
    public val firestoreAccountFile: File get() = checkNotNull(firestoreAccountFileInternal) { "Firestore account file is not initialized" }
}
