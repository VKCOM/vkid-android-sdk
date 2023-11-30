package com.vk.id.onetap.xml

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonElevationStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle
import com.vk.id.onetap.compose.onetap.sheet.OneTapScenario
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapSheetCornersStyle

internal fun parseOneTapAttrs(
    context: Context,
    attrs: AttributeSet?,
): OneTapParsedAttrs {
    context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.vkid_OneTap,
        0,
        0
    ).apply {
        try {
            return OneTapParsedAttrs(
                style = getOneTapStyleConstructor()(
                    OneTapButtonCornersStyle.Custom(context.pixelsToDp(getButtonsCornerRadius(context))),
                    getOneTapButtonsSize(),
                    OneTapButtonElevationStyle.Custom(context.pixelsToDp(getOneTapButtonsElevation(context)))
                ),
                isSignInToAnotherAccountEnabled = getSignInToAnotherAccountButtonEnabled(),
                oAuths = getOAuths(),
            )
        } finally {
            recycle()
        }
    }
}

internal data class OneTapParsedAttrs(
    val style: OneTapStyle,
    val isSignInToAnotherAccountEnabled: Boolean,
    val oAuths: Set<OneTapOAuth>,
)

internal class OneTapBottomSheetAttributeSettings(
    val style: OneTapBottomSheetStyle,
    val serviceName: String,
    val scenario: OneTapScenario,
    val autoHideOnSuccess: Boolean,
    val oAuths: Set<OneTapOAuth>,
)

internal fun parseOneTapBottomSheetAttrs(
    context: Context,
    attrs: AttributeSet?,
): OneTapBottomSheetAttributeSettings {
    context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.vkid_OneTap,
        0,
        0
    ).apply {
        try {
            return OneTapBottomSheetAttributeSettings(
                style = getSheetStyleConstructor()(
                    OneTapSheetCornersStyle.Custom(context.pixelsToDp(getSheetCornerRadius(context))),
                    OneTapButtonCornersStyle.Custom(context.pixelsToDp(getButtonsCornerRadius(context))),
                    getOneTapButtonsSize(),
                ),
                serviceName = getSheetServiceName(),
                scenario = getSheetScenario(),
                autoHideOnSuccess = getSheetAutoHideOnSuccess(),
                oAuths = getOAuths(),
            )
        } finally {
            recycle()
        }
    }
}

private fun TypedArray.getSheetCornerRadius(context: Context) = getDimension(
    R.styleable.vkid_OneTap_vkid_bottomSheetCornerRadius,
    context.dpToPixels(OneTapSheetCornersStyle.Default.radiusDp)
)

private fun TypedArray.getButtonsCornerRadius(context: Context) = getDimension(
    R.styleable.vkid_OneTap_vkid_buttonsCornerRadius,
    context.dpToPixels(OneTapButtonCornersStyle.Default.radiusDp)
)

private fun TypedArray.getOneTapButtonsElevation(context: Context) = getDimension(
    R.styleable.vkid_OneTap_vkid_buttonsElevation,
    context.dpToPixels(OneTapButtonElevationStyle.Default.elevationDp)
)

@Suppress("MagicNumber")
private fun TypedArray.getOneTapStyleConstructor() = when (getInt(R.styleable.vkid_OneTap_vkid_onetapStyle, 0)) {
    1 -> OneTapStyle::Dark
    2 -> OneTapStyle::TransparentLight
    3 -> OneTapStyle::TransparentDark
    4 -> OneTapStyle::Icon
    else -> OneTapStyle::Light
}

@Suppress("MagicNumber")
private fun TypedArray.getSheetStyleConstructor() = when (getInt(R.styleable.vkid_OneTap_vkid_bottomSheetStyle, 0)) {
    1 -> OneTapBottomSheetStyle::Dark
    2 -> OneTapBottomSheetStyle::TransparentLight
    3 -> OneTapBottomSheetStyle::TransparentDark
    else -> OneTapBottomSheetStyle::Light
}

@Suppress("MagicNumber", "CyclomaticComplexMethod")
private fun TypedArray.getOneTapButtonsSize() = when (getInt(R.styleable.vkid_OneTap_vkid_buttonsSize, 0)) {
    1 -> OneTapButtonSizeStyle.SMALL_32
    2 -> OneTapButtonSizeStyle.SMALL_34
    3 -> OneTapButtonSizeStyle.SMALL_36
    4 -> OneTapButtonSizeStyle.SMALL_38
    5 -> OneTapButtonSizeStyle.MEDIUM_40
    6 -> OneTapButtonSizeStyle.MEDIUM_42
    7 -> OneTapButtonSizeStyle.MEDIUM_44
    8 -> OneTapButtonSizeStyle.MEDIUM_46
    9 -> OneTapButtonSizeStyle.LARGE_48
    10 -> OneTapButtonSizeStyle.LARGE_50
    11 -> OneTapButtonSizeStyle.LARGE_52
    12 -> OneTapButtonSizeStyle.LARGE_54
    13 -> OneTapButtonSizeStyle.LARGE_56
    else -> OneTapButtonSizeStyle.DEFAULT
}

private fun TypedArray.getSignInToAnotherAccountButtonEnabled(): Boolean {
    return getBoolean(R.styleable.vkid_OneTap_vkid_onetapShowSignInToAnotherAccount, false)
}

private fun TypedArray.getSheetServiceName(): String {
    return getString(R.styleable.vkid_OneTap_vkid_bottomSheetServiceName) ?: ""
}

private fun TypedArray.getSheetAutoHideOnSuccess(): Boolean {
    return getBoolean(R.styleable.vkid_OneTap_vkid_autoHideOnSuccess, true)
}

@Suppress("MagicNumber")
private fun TypedArray.getSheetScenario() = when (getInt(R.styleable.vkid_OneTap_vkid_bottomSheetScenario, 0)) {
    1 -> OneTapScenario.RegistrationForTheEvent
    2 -> OneTapScenario.Application
    3 -> OneTapScenario.OrderInService
    4 -> OneTapScenario.Order
    5 -> OneTapScenario.EnterToAccount
    else -> OneTapScenario.EnterService
}

private fun TypedArray.getOAuths(): Set<OneTapOAuth> {
    return (getString(R.styleable.vkid_OneTap_vkid_onetapOAuths) ?: "")
        .split(',')
        .filter { it.isNotBlank() }
        .map {
            when (it) {
                "mail" -> OneTapOAuth.MAIL
                "ok" -> OneTapOAuth.OK
                else -> error("""Unexpected oauth "$it", please use one of "mail" or "ok", separated by commas""")
            }
        }
        .toSet()
}

private fun Context.pixelsToDp(
    px: Float
) = px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

private fun Context.dpToPixels(
    dp: Float
) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
