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
import com.vk.id.common.InternalVKIDApi

@Suppress("TooManyFunctions")
internal object OneTapAnalytics {

    private var userWasFound = false

    internal fun userWasFoundIcon() {
        userWasFound(signInAnotherAccountButton = false, icon = true)
    }

    internal fun userWasFound(signInAnotherAccountButton: Boolean, icon: Boolean = false) {
        track(
            "onetap_button_user_found",
            alternateParam(signInAnotherAccountButton),
            iconParam(icon)
        )
        userWasFound = true
    }

    @Composable
    internal fun OneTapIconShown() {
        OneTapShown(icon = true)
    }

    @Composable
    internal fun OneTapShown(signInAnotherAccountButton: Boolean = false, icon: Boolean = false) {
        val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
        DisposableEffect(lifecycleOwner.value) {
            val lifecycle = lifecycleOwner.value.lifecycle
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        track(
                            "onetap_button_no_user_show",
                            alternateParam(signInAnotherAccountButton),
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

    internal fun oneTapPressedIcon(user: VKIDUser?) {
        oneTapPressed(user, icon = true)
    }

    internal fun oneTapPressed(user: VKIDUser?, icon: Boolean = false) {
        if (user != null) {
            track("onetap_button_tap", iconParam(icon))
        } else {
            track("onetap_button_no_user_tap", iconParam(icon))
        }
    }

    internal fun alternatePressed() {
        track("onetap_button_alternative_sign_in_tap")
    }

    internal fun authSuccessIcon() {
        authSuccess(true)
    }

    internal fun authSuccess(icon: Boolean = false) {
        track("auth_by_button", iconParam(icon))
    }

    internal fun authErrorIcon(user: VKIDUser?) {
        authError(user, true)
    }

    internal fun authError(user: VKIDUser?, icon: Boolean = false) {
        if (user != null) {
            track("onetap_button_auth_error", iconParam(icon))
        } else {
            track("onetap_button_no_user_auth_error", iconParam(icon))
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

    private fun alternateParam(signInAnotherAccountButton: Boolean): VKIDAnalytics.EventParam =
        VKIDAnalytics.EventParam(
            "alternative_sign_in_availability",
            if (signInAnotherAccountButton) {
                "available"
            } else {
                "not_available"
            }
        )

    private fun track(name: String, vararg params: VKIDAnalytics.EventParam) {
        VKIDAnalytics.trackEvent(
            name,
            VKIDAnalytics.EventParam("sdk_type", "vkid"),
            *params
        )
    }
}
