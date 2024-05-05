package com.vk.id.health.metrics.storage

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import com.vk.id.health.metrics.VKIDHealthMetricsExtension
import java.io.File
import java.io.FileInputStream


fun VKIDHealthMetricsExtension.firestore(serviceAccountFile: File) {
    val options: FirebaseOptions = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(FileInputStream(serviceAccountFile)))
        .build()

    if (FirebaseApp.getApps().all { it.name != FirebaseApp.DEFAULT_APP_NAME }) {
        FirebaseApp.initializeApp(options)
    }

    firestoreInternal = FirestoreClient.getFirestore()
}