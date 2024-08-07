@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.analytics

import com.vk.id.OAuth
import com.vk.id.StatParams
import com.vk.id.analytics.VKIDAnalytics
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.common.InternalVKIDApi
import java.util.UUID

internal fun OAuth?.toAnalyticsParam(): String =
    when (this) {
        OAuth.VK -> "vk"
        OAuth.MAIL -> "mail_ru"
        OAuth.OK -> "ok_ru"
        null -> ""
    }

internal object CustomAuthAnalytics {
    internal fun customAuthStart(params: VKIDAuthParams): StatParams {
        val authEventUUId = UUID.randomUUID().toString()
        VKIDAnalytics.trackEvent(
            "custom_auth_start",
            VKIDAnalytics.EventParam("sdk_type", "vkid"),
            VKIDAnalytics.EventParam("unique_session_id", authEventUUId),
            VKIDAnalytics.EventParam("oauth_service", params.oAuth.toAnalyticsParam())
        )
        return StatParams(
            flowSource = "from_custom_auth",
            sessionId = authEventUUId
        )
    }

    internal fun customAuthError(statParams: StatParams) {
        VKIDAnalytics.trackEvent(
            "sdk_auth_error",
            VKIDAnalytics.EventParam("error", "sdk_auth_error"),
            VKIDAnalytics.EventParam("sdk_type", "vkid"),
            VKIDAnalytics.EventParam("unique_session_id", statParams.sessionId),
            VKIDAnalytics.EventParam("from_custom_auth", "true")
        )
    }
}
