package com.vk.id.analytics.param

import android.content.Context
import android.os.Build
import com.vk.id.analytics.VKIDAnalytics
import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public fun vkidInternalLanguageParam(context: Context): VKIDAnalytics.EventParam {
    val systemLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales.get(0)
    } else {
        @Suppress("DEPRECATION")
        context.resources.configuration.locale
    }

    @Suppress("MagicNumber")
    val vkidLocale = when (systemLocale.language) {
        "ru" -> 0
        "uk" -> 1
        "en" -> 3
        "es" -> 4
        "de" -> 6
        "pl" -> 15
        "fr" -> 16
        "tr" -> 82
        else -> 3
    }
    return VKIDAnalytics.EventParam("language", strValue = vkidLocale.toString())
}
