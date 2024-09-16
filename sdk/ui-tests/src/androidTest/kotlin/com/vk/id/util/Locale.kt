package com.vk.id.util

import android.content.Context
import android.os.Build

/**
 * Returns the current system locale.
 */
internal fun systemLocaleForProviderParam(context: Context): String? {
    val systemLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales.get(0)
    } else {
        @Suppress("DEPRECATION")
        context.resources.configuration.locale
    }
    return when (systemLocale.language) {
        "ru" -> "0"
        "uk" -> "1"
        "en" -> "3"
        "es" -> "4"
        "de" -> "6"
        "pl" -> "15"
        "fr" -> "16"
        "tr" -> "82"
        else -> null
    }
}
