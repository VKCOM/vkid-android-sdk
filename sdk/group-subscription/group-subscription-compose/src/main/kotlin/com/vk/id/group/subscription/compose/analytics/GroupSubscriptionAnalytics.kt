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
import com.vk.id.analytics.param.vkidInternalLanguageParam
import com.vk.id.common.InternalVKIDApi
import com.vk.id.group.subscription.common.style.GroupSubscriptionStyle
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

@Suppress("TooManyFunctions")
internal object GroupSubscriptionAnalytics {

    internal val isErrorState = AtomicBoolean(false)
    internal val style = AtomicReference<GroupSubscriptionStyle?>(null)
    internal val groupId = AtomicReference<String?>(null)

    @Composable
    internal fun SheetShown(accessToken: String?) {
        SheetScreenShown {
            track("community_follow_modal_window_show", accessToken = accessToken)
        }
    }

    internal fun subscribeToGroupClick(accessToken: String?) = track("community_follow_click", accessToken = accessToken)

    internal fun nextTimeClick(accessToken: String?) = track("community_follow_next_time_click", accessToken = accessToken)

    internal fun close(accessToken: String?) {
        if (isErrorState.get()) {
            track("community_follow_error_close", accessToken = accessToken)
        } else {
            track("community_follow_close", accessToken = accessToken)
        }
    }

    @Composable
    internal fun ErrorShown(accessToken: String?) {
        SheetScreenShown {
            track("community_follow_error_show", accessToken = accessToken)
        }
    }

    internal fun retryClick(accessToken: String?) = track("community_follow_error_retry_click", accessToken = accessToken)

    internal fun cancelClick(accessToken: String?) = track("community_follow_error_cancel_click", accessToken = accessToken)

    internal fun successShown(accessToken: String?) = track("community_follow_success", accessToken = accessToken)

    private fun track(eventName: String, accessToken: String?) {
        VKIDAnalytics.trackEvent(
            accessToken = accessToken,
            eventName,
            nowhereScreen(),
            appIdParam(),
            vkidInternalLanguageParam(VKID.instance.context),
            themeParam(),
            groupIdParam()
        )
    }

    private fun appIdParam() = VKIDAnalytics.EventParam("app_id", intValue = VKID.instance.clientId.toIntOrNull())
    private fun nowhereScreen() = VKIDAnalytics.EventParam("screen_current", "nowhere")
    private fun groupIdParam() = VKIDAnalytics.EventParam("group_id", strValue = groupId.get())
    private fun themeParam() = VKIDAnalytics.EventParam(
        "theme_type",
        when (style.get()) {
            null -> "light"
            is GroupSubscriptionStyle.Light -> "light"
            is GroupSubscriptionStyle.Dark -> "dark"
        }
    )

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
