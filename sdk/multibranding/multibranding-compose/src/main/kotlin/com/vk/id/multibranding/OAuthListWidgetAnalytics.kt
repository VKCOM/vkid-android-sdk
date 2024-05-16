@file:OptIn(InternalVKIDApi::class)

package com.vk.id.multibranding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.vk.id.OAuth
import com.vk.id.analytics.VKIDAnalytics
import com.vk.id.common.InternalVKIDApi
import java.util.UUID

@Suppress("TooManyFunctions")
internal class OAuthListWidgetAnalytics(private val screen: String, private val paused: Boolean) {

    fun oauthAdded(oAuths: Set<OAuth>) {
        val oauthParam: (String, OAuth) -> VKIDAnalytics.EventParam = { name, oauth ->
            val value = if (oAuths.contains(oauth)) "1" else "0"
            VKIDAnalytics.EventParam(name, value)
        }
        track(
            "multibranding_oauth_added",
            oauthParam("ok", OAuth.OK),
            oauthParam("mail", OAuth.MAIL),
            oauthParam("vk", OAuth.VK)
        )
    }

    @Composable
    fun OAuthShown(oAuth: OAuth, isText: Boolean) {
        val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
        DisposableEffect(lifecycleOwner.value) {
            val lifecycle = lifecycleOwner.value.lifecycle
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        val name = when (oAuth) {
                            OAuth.VK -> "vk_button_show"
                            OAuth.MAIL -> "mail_button_show"
                            OAuth.OK -> "ok_button_show"
                        }
                        track(name, isIconParam(isText))
                    }
                    else -> {}
                }
            }

            lifecycle.addObserver(observer)
            onDispose {
                lifecycle.removeObserver(observer)
            }
        }
    }

    fun onOAuthTap(oAuth: OAuth, isText: Boolean): Map<String, String> {
        val uuid = UUID.randomUUID().toString()
        val name = when (oAuth) {
            OAuth.VK -> "vk_button_tap"
            OAuth.MAIL -> "mail_button_tap"
            OAuth.OK -> "ok_button_tap"
        }
        track(name, isIconParam(isText), uuidParam(uuid))
        return mapOf(UNIQUE_SESSION_PARAM_NAME to uuid)
    }

    fun onAuthSuccess(oAuth: OAuth, uuid: String) {
        val oauth = when (oAuth) {
            OAuth.VK -> return // no tracking
            OAuth.MAIL -> "mail_ru"
            OAuth.OK -> "ok_ru"
        }
        track("auth_by_oauth", VKIDAnalytics.EventParam("oauth_service", oauth), uuidParam(uuid))
    }

    fun onAuthError(sessionId: String) {
        if (!paused) {
            VKIDAnalytics.trackEvent(
                "multibranding_auth_error",
                VKIDAnalytics.EventParam("sdk_type", "vkid"),
                uuidParam(sessionId),
                VKIDAnalytics.EventParam("error", "auth_error"),
                VKIDAnalytics.EventParam("screen_current", screen)
            )
        }
    }

    private fun isIconParam(isText: Boolean) =
        VKIDAnalytics.EventParam("button_type", if (isText) "default" else "icon")

    private fun uuidParam(uuid: String) = VKIDAnalytics.EventParam(UNIQUE_SESSION_PARAM_NAME, uuid)

    private fun track(name: String, vararg params: VKIDAnalytics.EventParam) {
        if (!paused) {
            VKIDAnalytics.trackEvent(
                name,
                VKIDAnalytics.EventParam("sdk_type", "vkid"),
                VKIDAnalytics.EventParam("screen", screen),
                *params
            )
        }
    }

    internal companion object {
        const val UNIQUE_SESSION_PARAM_NAME = "unique_session_id"
    }
}
