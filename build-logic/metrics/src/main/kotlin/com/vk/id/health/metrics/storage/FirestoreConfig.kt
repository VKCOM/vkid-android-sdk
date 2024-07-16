package com.vk.id.health.metrics.storage

import com.vk.id.health.metrics.VKIDHealthMetricsExtension
import java.io.File

public fun VKIDHealthMetricsExtension.firestore(serviceAccountFile: File) {
    FirestoreHolder.serviceAccountFile = serviceAccountFile
}
