@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.onetap

import android.content.Context
import android.os.Build
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
import com.vk.id.analytics.stat.StatTracker
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.OneTapStyle
import java.util.UUID

@Suppress("TooManyFunctions")
internal object OneTapAnalytics {

    private const val EVENT_SCREEN_PROCEED = "screen_proceed"
    private const val EVENT_USER_FOUND = "onetap_button_user_found"
    private const val EVENT_ONETAP_TAP = "onetap_button_tap"
    private const val EVENT_ONETAP_ALTERNATIVE_SIGN_IN_TAP = "onetap_button_alternative_sign_in_tap"
    private const val EVENT_ONETAP_AUTH_ERROR = "sdk_auth_error"
    private const val EVENT_NO_SESSION_FOUND = "no_session_found"
    private const val EVENT_NO_USER_SHOW = "onetap_button_no_user_show"
    private const val EVENT_ONETAP_NO_USER_TAP = "onetap_button_no_user_tap"

    internal fun sessionNotFound() {
        VKID.instance.crashReporter.runReportingCrashes({}) {
            track(EVENT_NO_SESSION_FOUND)
        }
    }

    internal fun userWasFoundIcon() {
        userWasFound(signInAnotherAccountButton = false, icon = true)
    }

    internal fun userWasFound(signInAnotherAccountButton: Boolean, icon: Boolean = false) {
        VKID.instance.crashReporter.runReportingCrashes({}) {
            track(
                EVENT_USER_FOUND,
                alternateParam(signInAnotherAccountButton),
                iconParam(icon)
            )
        }
    }

    internal fun userNotFoundIcon() {
        userNotFound(true)
    }

    internal fun userNotFound(icon: Boolean = false) {
        VKID.instance.crashReporter.runReportingCrashes({}) {
            track(
                EVENT_NO_USER_SHOW,
                iconParam(icon)
            )
        }
    }

    @Composable
    internal fun OneTapIconShown(scenario: OneTapTitleScenario, style: OneTapStyle) {
        OneTapShown(icon = true, scenario = scenario, style = style)
    }

    @Composable
    internal fun OneTapShown(icon: Boolean = false, scenario: OneTapTitleScenario, style: OneTapStyle) {
        val context = LocalContext.current
        val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
        DisposableEffect(lifecycleOwner.value) {
            val lifecycle = lifecycleOwner.value.lifecycle
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        VKID.instance.crashReporter.runReportingCrashes({}) {
                            track(
                                EVENT_SCREEN_PROCEED,
                                iconParam(icon),
                                textTypeParam(scenario),
                                themeParam(style),
                                styleParam(style),
                                langParam(context)
                            )
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

    internal fun oneTapPressedIcon(user: VKIDUser?): Map<String, String> {
        return oneTapPressed(user, icon = true)
    }

    internal fun oneTapPressed(user: VKIDUser?, icon: Boolean = false): Map<String, String> {
        val uuid = UUID.randomUUID().toString()
        VKID.instance.crashReporter.runReportingCrashes({}) {
            if (user != null) {
                track(EVENT_ONETAP_TAP, iconParam(icon), uuidParam(uuid))
            } else {
                track(EVENT_ONETAP_NO_USER_TAP, iconParam(icon), uuidParam(uuid))
            }
        }
        return mapOf(StatTracker.EXTERNAL_PARAM_SESSION_ID to uuid, FLOW_SOURCE)
    }

    internal fun alternatePressed(): Map<String, String> {
        val uuid = UUID.randomUUID().toString()
        VKID.instance.crashReporter.runReportingCrashes({}) {
            track(EVENT_ONETAP_ALTERNATIVE_SIGN_IN_TAP, uuidParam(uuid))
        }
        return mapOf(StatTracker.EXTERNAL_PARAM_SESSION_ID to uuid, FLOW_SOURCE)
    }

    internal fun authErrorIcon(uuid: String) {
        authError(uuid, true)
    }

    internal fun authError(uuid: String, icon: Boolean = false) {
        VKID.instance.crashReporter.runReportingCrashes({}) {
            track(
                EVENT_ONETAP_AUTH_ERROR,
                iconParam(icon),
                uuidParam(uuid),
                VKIDAnalytics.EventParam("from_one_tap", "true"),
                VKIDAnalytics.EventParam("error", "sdk_auth_error")
            )
        }
    }

    private fun themeParam(style: OneTapStyle): VKIDAnalytics.EventParam =
        VKIDAnalytics.EventParam(
            "theme_type",
            @Suppress("DEPRECATION")
            when (style) {
                is OneTapStyle.Dark,
                is OneTapStyle.TransparentDark,
                is OneTapStyle.SecondaryDark,
                is OneTapStyle.Icon -> "dark"

                is OneTapStyle.Light,
                is OneTapStyle.SecondaryLight,
                is OneTapStyle.TransparentLight -> "light"
            }
        )

    private fun styleParam(style: OneTapStyle): VKIDAnalytics.EventParam =
        VKIDAnalytics.EventParam(
            "style_type",
            @Suppress("DEPRECATION")
            when (style) {
                is OneTapStyle.Dark,
                is OneTapStyle.Icon,
                is OneTapStyle.Light -> "primary"

                is OneTapStyle.SecondaryDark,
                is OneTapStyle.SecondaryLight,
                is OneTapStyle.TransparentDark,
                is OneTapStyle.TransparentLight -> "secondary"
            }
        )

    private fun iconParam(icon: Boolean): VKIDAnalytics.EventParam =
        VKIDAnalytics.EventParam(
            "button_type",
            if (icon) {
                "icon"
            } else {
                "default"
            }
        )

    private fun textTypeParam(scenario: OneTapTitleScenario) = VKIDAnalytics.EventParam(
        "text_type",
        when (scenario) {
            OneTapTitleScenario.SignIn -> "default"
            OneTapTitleScenario.SignUp -> "appoint"
            OneTapTitleScenario.Get -> "receive"
            OneTapTitleScenario.Open -> "open"
            OneTapTitleScenario.Calculate -> "calculate"
            OneTapTitleScenario.Order -> "order"
            OneTapTitleScenario.PlaceOrder -> "service_order_placing"
            OneTapTitleScenario.SendRequest -> "request"
            OneTapTitleScenario.Participate -> "take_part"
        }
    )

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

    internal fun langParam(context: Context): VKIDAnalytics.EventParam {
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

    internal fun Map<String, String>.uuidFromParams(): String {
        return this[StatTracker.EXTERNAL_PARAM_SESSION_ID] ?: ""
    }

    internal const val SCREEN_PARAM_NAME = "screen"
    private val FLOW_SOURCE = StatTracker.EXTERNAL_PARAM_FLOW_SOURCE to "from_one_tap"
}
