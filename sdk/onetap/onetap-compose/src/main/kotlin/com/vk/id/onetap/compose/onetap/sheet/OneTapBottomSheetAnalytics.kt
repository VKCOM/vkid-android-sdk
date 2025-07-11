@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.onetap.sheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.vk.id.VKID
import com.vk.id.VKIDUser
import com.vk.id.analytics.VKIDAnalytics
import com.vk.id.analytics.param.vkidInternalLanguageParam
import com.vk.id.analytics.stat.StatTracker
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.compose.onetap.OneTapAnalytics
import com.vk.id.onetap.compose.onetap.OneTapAnalytics.SCREEN_PARAM_NAME
import com.vk.id.onetap.compose.onetap.OneTapAnalytics.track
import java.util.UUID

@Suppress("TooManyFunctions")
internal object OneTapBottomSheetAnalytics {

    @Composable
    internal fun OneTapBottomSheetShown(theme: VKIDAuthParams.Theme, scenario: OneTapScenario) {
        val context = LocalContext.current
        SheetScreenShown {
            track(
                "screen_proceed",
                VKIDAnalytics.EventParam("screen_current", "nowhere"),
                VKIDAnalytics.EventParam("screen_to", "floating_one_tap"),
                theme.toAnalyticsParam(),
                scenario.toAnalyticsParam(),
                vkidInternalLanguageParam(context),
            )
        }
    }

    @Composable
    internal fun BottomSheetInProgressShown() {
        SheetScreenShown {
            track(
                "data_loading",
                screenParam,
            )
        }
    }

    @Composable
    internal fun BottomSheetErrorShown() {
        SheetScreenShown {
            track(
                "alert_auth_error",
                VKIDAnalytics.EventParam("screen_current", "floating_one_tap"),
                screenParam,
            )
        }
    }

    @Composable
    private fun SheetScreenShown(fireAnalytics: () -> Unit) {
        val rememberedFireAnalytics = rememberUpdatedState(fireAnalytics)
        val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
        DisposableEffect(lifecycleOwner.value) {
            val lifecycle = lifecycleOwner.value.lifecycle
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        VKID.instance.crashReporter.runReportingCrashes({}) {
                            rememberedFireAnalytics.value()
                        }
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

    internal fun noActiveSession() {
        VKID.instance.crashReporter.runReportingCrashes({}) {
            track("no_active_session", screenParam)
        }
    }

    internal fun noUserButtonShown() {
        VKID.instance.crashReporter.runReportingCrashes({}) {
            track("no_user_button_show", screenParam)
        }
    }

    internal fun oneTapPressed(user: VKIDUser?): Map<String, String> {
        val uuid = UUID.randomUUID().toString()
        VKID.instance.crashReporter.runReportingCrashes({}) {
            if (user == null) {
                track("no_user_button_tap", OneTapAnalytics.uuidParam(uuid), screenParam)
            } else {
                track("continue_as_tap", OneTapAnalytics.uuidParam(uuid), screenParam)
            }
        }
        return mapOf(StatTracker.EXTERNAL_PARAM_SESSION_ID to uuid, FLOW_SOURCE)
    }

    internal fun alternatePressed(): Map<String, String> {
        val uuid = UUID.randomUUID().toString()
        VKID.instance.crashReporter.runReportingCrashes({}) {
            track("alternative_sign_in_tap", OneTapAnalytics.uuidParam(uuid), screenParam)
        }
        return mapOf(StatTracker.EXTERNAL_PARAM_SESSION_ID to uuid, FLOW_SOURCE)
    }

    internal fun userWasFound(signInAnotherAccountButton: Boolean) {
        VKID.instance.crashReporter.runReportingCrashes({}) {
            track(
                "continue_as_show",
                OneTapAnalytics.alternateParam(signInAnotherAccountButton),
                screenParam
            )
        }
    }

    internal fun retryAuthTap(): Map<String, String> {
        val uuid = UUID.randomUUID().toString()
        VKID.instance.crashReporter.runReportingCrashes({}) {
            track("retry_auth_tap", screenParam)
        }
        return mapOf(StatTracker.EXTERNAL_PARAM_SESSION_ID to uuid, FLOW_SOURCE)
    }

    internal fun authError(uuid: String) {
        VKID.instance.crashReporter.runReportingCrashes({}) {
            track(
                "sdk_auth_error",
                OneTapAnalytics.uuidParam(uuid),
                VKIDAnalytics.EventParam("from_floating_one_tap", "true"),
                VKIDAnalytics.EventParam("error", "sdk_auth_error")
            )
        }
    }

    private val screenParam = VKIDAnalytics.EventParam(SCREEN_PARAM_NAME, "floating_one_tap")

    private val FLOW_SOURCE = StatTracker.EXTERNAL_PARAM_FLOW_SOURCE to "from_floating_one_tap"
}

private fun OneTapScenario.toAnalyticsParam(): VKIDAnalytics.EventParam {
    val scenarioParam = when (this) {
        OneTapScenario.EnterService -> "service_sign_in"
        OneTapScenario.RegistrationForTheEvent -> "event_reg"
        OneTapScenario.Application -> "request"
        OneTapScenario.OrderInService -> "service_order_placing"
        OneTapScenario.Order -> "vkid_order_placing"
        OneTapScenario.EnterToAccount -> "account_sign_in"
    }
    return VKIDAnalytics.EventParam("text_type", scenarioParam)
}

private fun VKIDAuthParams.Theme.toAnalyticsParam(): VKIDAnalytics.EventParam {
    val themeParam = if (this == VKIDAuthParams.Theme.Light) {
        "light"
    } else {
        "dark"
    }
    return VKIDAnalytics.EventParam("theme_type", themeParam)
}
