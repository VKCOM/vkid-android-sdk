package com.vk.id.health.metrics.storage

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import java.io.File
import java.io.FileInputStream

internal object FirestoreHolder {
    var serviceAccountFile: File? = null
    val instance: Firestore by lazy {
        val options: FirebaseOptions = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(FileInputStream(serviceAccountFile ?: error("Firestore is not initialized"))))
            .build()

        if (FirebaseApp.getApps().all { it.name != FirebaseApp.DEFAULT_APP_NAME }) {
            FirebaseApp.initializeApp(options)
        }
        FirestoreClient.getFirestore()
    }
}
