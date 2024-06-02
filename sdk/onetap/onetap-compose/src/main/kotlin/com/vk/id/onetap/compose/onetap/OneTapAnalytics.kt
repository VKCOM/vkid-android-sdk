@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.onetap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.vk.id.VKIDUser
import com.vk.id.analytics.VKIDAnalytics
import com.vk.id.analytics.stat.StatTracker
import com.vk.id.common.InternalVKIDApi
import java.util.UUID

@Suppress("TooManyFunctions")
internal object OneTapAnalytics {

    // https://confluence.vk.team/pages/viewpage.action?pageId=1099030792
    private const val EVENT_SCREEN_PROCEED = "screen_proceed"
    private const val EVENT_USER_FOUND = "onetap_button_user_found"
    private const val EVENT_ONETAP_TAP = "onetap_button_tap"
    private const val EVENT_AUTH_BY_BUTTON = "auth_by_button"
    private const val EVENT_ONETAP_ALTERNATIVE_SIGN_IN_TAP = "onetap_button_alternative_sign_in_tap"
    private const val EVENT_ONETAP_AUTH_ERROR= "onetap_button_auth_error"
    private const val EVENT_NO_SESSION_FOUND = "no_session_found"
    private const val EVENT_NO_USER_SHOW = "onetap_button_no_user_show"
    private const val EVENT_ONETAP_NO_USER_TAP = "onetap_button_no_user_tap"
    private const val EVENT_ONETAP_NO_USER_AUTH_ERROR = "onetap_button_no_user_auth_error"

    // Загрузка кнопки, ищем сессию у пользователя
    internal fun tryToFoundUserEvent() {
        track(
            EVENT_SCREEN_PROCEED,
            VKIDAnalytics.EventParam("screen_current", "nowhere")
        )
    }

    internal fun userWasFoundIcon() {
        userWasFound(signInAnotherAccountButton = false, icon = true)
    }

    internal fun userWasFound(signInAnotherAccountButton: Boolean, icon: Boolean = false) {
        track(
            EVENT_USER_FOUND,
            alternateParam(signInAnotherAccountButton),
            iconParam(icon)
        )
    }

    @Composable
    internal fun OneTapIconShown() {
        OneTapShown(icon = true)
    }

    @Composable
    internal fun OneTapShown(icon: Boolean = false) {
        val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
        DisposableEffect(lifecycleOwner.value) {
            val lifecycle = lifecycleOwner.value.lifecycle
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        track(
                            EVENT_NO_USER_SHOW,
                            iconParam(icon)
                        )
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

    internal fun oneTapPressedIcon(user: VKIDUser?): Map<String, String> {
        return oneTapPressed(user, icon = true)
    }

    internal fun oneTapPressed(user: VKIDUser?, icon: Boolean = false): Map<String, String> {
        val uuid = UUID.randomUUID().toString()
        if (user != null) {
            track(EVENT_ONETAP_TAP, iconParam(icon), uuidParam(uuid))
        } else {
            track(EVENT_ONETAP_NO_USER_TAP, iconParam(icon), uuidParam(uuid))
        }
        return mapOf(StatTracker.EXTERNAL_PARAM_SESSION_ID to uuid, FLOW_SOURCE)
    }

    internal fun alternatePressed(): Map<String, String> {
        val uuid = UUID.randomUUID().toString()
        track(EVENT_ONETAP_ALTERNATIVE_SIGN_IN_TAP, uuidParam(uuid))
        return mapOf(StatTracker.EXTERNAL_PARAM_SESSION_ID to uuid, FLOW_SOURCE)
    }

    internal fun authSuccessIcon() {
        authSuccess(true)
    }

    internal fun authSuccess(icon: Boolean = false) {
        track(EVENT_AUTH_BY_BUTTON, iconParam(icon))
    }

    internal fun authErrorIcon(user: VKIDUser?) {
        authError(user, true)
    }

    internal fun authError(user: VKIDUser?, icon: Boolean = false) {
        if (user != null) {
            track(EVENT_ONETAP_AUTH_ERROR, iconParam(icon))
        } else {
            track(EVENT_ONETAP_NO_USER_AUTH_ERROR, iconParam(icon))
        }
    }

    private fun iconParam(icon: Boolean): VKIDAnalytics.EventParam =
        VKIDAnalytics.EventParam(
            "button_type",
            if (icon) {
                "icon"
            } else {
                "default"
            }
        )

    internal fun alternateParam(signInAnotherAccountButton: Boolean): VKIDAnalytics.EventParam =
        VKIDAnalytics.EventParam(
            "alternative_sign_in_availability",
            if (signInAnotherAccountButton) {
                "available"
            } else {
                "not_available"
            }
        )

    internal fun uuidParam(uuid: String) = VKIDAnalytics.EventParam("unique_session_id", uuid)

    internal fun track(name: String, vararg params: VKIDAnalytics.EventParam) {
        VKIDAnalytics.trackEvent(
            name,
            VKIDAnalytics.EventParam("sdk_type", "vkid"),
            *params
        )
    }

    internal const val SCREEN_PARAM_NAME = "screen"
    private val FLOW_SOURCE = StatTracker.EXTERNAL_PARAM_FLOW_SOURCE to "from_one_tap"
}
