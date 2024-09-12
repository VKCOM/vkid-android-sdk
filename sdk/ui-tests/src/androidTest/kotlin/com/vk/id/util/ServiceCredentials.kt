package com.vk.id.util

import android.content.ComponentName
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build

internal data class ServiceCredentials(
    val clientID: String,
    val clientSecret: String,
    val redirectUri: String
)

internal fun readVKIDCredentials(context: Context): ServiceCredentials {
    val componentName = ComponentName(context, "com.vk.id.internal.auth.AuthActivity")
    val flags = PackageManager.GET_META_DATA or PackageManager.GET_ACTIVITIES
    val ai: ActivityInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.packageManager.getActivityInfo(
            componentName,
            PackageManager.ComponentInfoFlags.of(flags.toLong())
        )
    } else {
        context.packageManager.getActivityInfo(
            componentName,
            flags
        )
    }
    val clientID = ai.metaData.getInt("VKIDClientID").toString()
    val clientSecret = ai.metaData.getString("VKIDClientSecret")!!
    val redirectScheme = ai.metaData.getString("VKIDRedirectScheme")
    val redirectHost = ai.metaData.getString("VKIDRedirectHost")
    val redirectUri = "$redirectScheme://$redirectHost/blank.html"

    return ServiceCredentials(clientID, clientSecret, redirectUri)
}
