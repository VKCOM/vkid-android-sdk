@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.onetap.sheet

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.vk.id.VKIDUser
import com.vk.id.analytics.VKIDAnalytics
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.compose.onetap.OneTapAnalytics
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
                screenParam,
                VKIDAnalytics.EventParam("screen_current", "nowhere"),
                VKIDAnalytics.EventParam("screen_to", "floating_one_tap"),
                theme.toAnalyticsParam(),
                scenario.toAnalyticsParam(),
                langParam(context)
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
    internal fun BottomSheetSuccessShown() {
        SheetScreenShown {
            track(
                "auth_by_floating_one_tap",
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
        val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
        DisposableEffect(lifecycleOwner.value) {
            val lifecycle = lifecycleOwner.value.lifecycle
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        fireAnalytics()
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
        track("no_active_session", screenParam)
    }

    internal fun noUserButtonShown() {
        track("no_user_button_show", screenParam)
    }

    internal fun oneTapPressed(user: VKIDUser?): Map<String, String> {
        val uuid = UUID.randomUUID().toString()
        if (user == null) {
            track("no_user_button_tap", OneTapAnalytics.uuidParam(uuid), screenParam)
        } else {
            track("continue_as_tap", OneTapAnalytics.uuidParam(uuid), screenParam)
        }
        return mapOf(OneTapAnalytics.UNIQUE_SESSION_PARAM_NAME to uuid)
    }

    internal fun userWasFound(signInAnotherAccountButton: Boolean) {
        track(
            "continue_as_show",
            OneTapAnalytics.alternateParam(signInAnotherAccountButton),
            screenParam
        )
    }

    internal fun retryAuthTap(): Map<String, String> {
        val uuid = UUID.randomUUID().toString()
        track("retry_auth_tap")
        return mapOf(OneTapAnalytics.UNIQUE_SESSION_PARAM_NAME to uuid)
    }

    private val screenParam = VKIDAnalytics.EventParam("screen", "floating_one_tap")

    private fun langParam(context: Context): VKIDAnalytics.EventParam {
        val systemLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
        val vkidLocale = when (systemLocale.language) {
            "ru" -> VKIDAuthParams.Locale.RUS
            "uk" -> VKIDAuthParams.Locale.UKR
            "en" -> VKIDAuthParams.Locale.ENG
            "es" -> VKIDAuthParams.Locale.SPA
            "de" -> VKIDAuthParams.Locale.GERMAN
            "pl" -> VKIDAuthParams.Locale.POL
            "fr" -> VKIDAuthParams.Locale.FRA
            "tr" -> VKIDAuthParams.Locale.TURKEY
            else -> VKIDAuthParams.Locale.ENG
        }
        return vkidLocale.toAnalyticsParam()
    }
}

@Suppress("MagicNumber")
private fun VKIDAuthParams.Locale.toAnalyticsParam(): VKIDAnalytics.EventParam {
    val langCode = when (this) {
        VKIDAuthParams.Locale.RUS -> 0
        VKIDAuthParams.Locale.UKR -> 1
        VKIDAuthParams.Locale.ENG -> 3
        VKIDAuthParams.Locale.SPA -> 4
        VKIDAuthParams.Locale.GERMAN -> 6
        VKIDAuthParams.Locale.POL -> 15
        VKIDAuthParams.Locale.FRA -> 16
        VKIDAuthParams.Locale.TURKEY -> 82
    }
    return VKIDAnalytics.EventParam("language", strValue = langCode.toString())
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
