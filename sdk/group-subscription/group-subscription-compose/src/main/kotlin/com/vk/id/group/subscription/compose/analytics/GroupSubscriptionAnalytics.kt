@file:OptIn(InternalVKIDApi::class)

package com.vk.id.group.subscription.compose.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.vk.id.VKID
import com.vk.id.analytics.VKIDAnalytics
import com.vk.id.common.InternalVKIDApi
import java.util.concurrent.atomic.AtomicBoolean

@Suppress("TooManyFunctions")
internal object GroupSubscriptionAnalytics {

    internal val isErrorState = AtomicBoolean(false)

    @Composable
    internal fun SheetShown() {
        SheetScreenShown {
            track("community_subscription_modal_window_show")
        }
    }

    internal fun subscribeToGroupClick() = track("community_subscription_click")

    internal fun nextTimeClick() = track("community_subscription_next_time_click")

    internal fun close() {
        if (isErrorState.get()) {
            track("community_subscription_error_close")
        } else {
            track("community_subscription_close")
        }
    }

    @Composable
    internal fun ErrorShown() {
        SheetScreenShown {
            track("community_subscription_error_show")
        }
    }

    internal fun retryClick() = track("community_subscription_error_retry_click")

    internal fun cancelClick() = track("community_subscription_error_cancel_click")

    internal fun successShown() = track("community_subscription_success")

    private fun track(eventName: String) {
        VKIDAnalytics.trackEvent(
            eventName,
            nowhereScreen(),
            appIdParam(),
        )
    }

    private fun appIdParam() = VKIDAnalytics.EventParam("app_id", VKID.instance.clientId)
    private fun nowhereScreen() = VKIDAnalytics.EventParam("screen_current", "nowhere")

    @Composable
    private fun SheetScreenShown(fireAnalytics: () -> Unit) {
        val rememberedFireAnalytics = rememberUpdatedState(fireAnalytics)
        val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
        DisposableEffect(lifecycleOwner.value) {
            val lifecycle = lifecycleOwner.value.lifecycle
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        rememberedFireAnalytics.value()
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
}
