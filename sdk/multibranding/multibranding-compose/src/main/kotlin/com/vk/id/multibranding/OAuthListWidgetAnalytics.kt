@file:OptIn(InternalVKIDApi::class)

package com.vk.id.multibranding

import com.vk.id.OAuth
import com.vk.id.analytics.VKIDAnalytics
import com.vk.id.common.InternalVKIDApi

@Suppress("TooManyFunctions")
internal class OAuthListWidgetAnalytics(screen: String) {

    private val screenParam = VKIDAnalytics.EventParam("screen", screen)

    fun oauthAdded(oAuths: Set<OAuth>) {
        val oauthParam: (String, OAuth) -> VKIDAnalytics.EventParam = { name, oauth ->
            val value = if (oAuths.contains(oauth)) "1" else "0"
            VKIDAnalytics.EventParam(name, value)
        }
        track(
            "multibranding_oauth_added",
            oauthParam("ok", OAuth.OK),
            oauthParam("mail", OAuth.MAIL),
            oauthParam("vk", OAuth.VK),
            screenParam
        )
    }

    private fun track(name: String, vararg params: VKIDAnalytics.EventParam) {
        VKIDAnalytics.trackEvent(
            name,
            VKIDAnalytics.EventParam("sdk_type", "vkid"),
            *params
        )
    }
}
