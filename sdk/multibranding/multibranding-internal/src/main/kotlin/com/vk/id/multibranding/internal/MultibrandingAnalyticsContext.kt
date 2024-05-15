package com.vk.id.multibranding.internal

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.vk.id.common.InternalVKIDApi

@InternalVKIDApi
public data class MultibrandingAnalyticsContext(public val screen: String, public val isPaused: Boolean = false)

@InternalVKIDApi
public val LocalMultibrandingAnalyticsContext: ProvidableCompositionLocal<MultibrandingAnalyticsContext> =
    staticCompositionLocalOf { MultibrandingAnalyticsContext(screen = "multibranding_widget", false) }
