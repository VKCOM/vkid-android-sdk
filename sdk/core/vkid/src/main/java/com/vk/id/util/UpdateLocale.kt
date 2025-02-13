package com.vk.id.util

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import com.vk.id.VKID
import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
@Composable
@SuppressLint("AppBundleLocaleChanges")
public fun InternalVKIDWithUpdatedLocale(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val locale = VKID.instance.internalVKIDLocale.get()

    val configuration = Configuration(context.resources.configuration)
    configuration.setLocale(locale)

    val newContext = context.createConfigurationContext(configuration)
    if (newContext != null) {
        CompositionLocalProvider(LocalContext provides newContext) {
            content()
        }
    } else {
        content()
    }
}
