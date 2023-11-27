package com.vk.id.onetap.xml

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
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
): Pair<OneTapStyle, Boolean> {
    context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.VKIDOneTap,
        0,
        0
    ).apply {
        try {
            return getOneTapStyleConstructor()(
                OneTapButtonCornersStyle.Custom(getButtonsCornerRadius().toInt()),
                getOneTapButtonsSize(),
                OneTapButtonElevationStyle.Custom(context.pixelsToDp(getOneTapButtonsElevation()))
            ) to getSignInToAnotherAccountButtonEnabled()
        } finally {
            recycle()
        }
    }
}

internal class OneTapBottomSheetAttributeSettings(
    val style: OneTapBottomSheetStyle,
    val serviceName: String,
    val scenario: OneTapScenario
)

internal fun parseOneTapBottomSheetAttrs(
    context: Context,
    attrs: AttributeSet?,
): OneTapBottomSheetAttributeSettings {
    context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.VKIDOneTap,
        0,
        0
    ).apply {
        try {
            return OneTapBottomSheetAttributeSettings(
                style = getSheetStyleConstructor()(
                    OneTapSheetCornersStyle.Custom(getSheetCornerRadius().toInt()),
                    OneTapButtonCornersStyle.Custom(getButtonsCornerRadius().toInt()),
                    getOneTapButtonsSize(),
                ),
                serviceName = getSheetServiceName(),
                scenario = getSheetScenario()
            )
        } finally {
            recycle()
        }
    }
}

private fun TypedArray.getSheetCornerRadius() = getDimension(
    R.styleable.VKIDOneTap_vkid_bottomSheetCornerRadius,
    OneTapSheetCornersStyle.Default.radiusDp.toFloat()
)

private fun TypedArray.getButtonsCornerRadius() = getDimension(
    R.styleable.VKIDOneTap_vkid_buttonsCornerRadius,
    OneTapButtonCornersStyle.Default.radiusDp.toFloat()
)

private fun TypedArray.getOneTapButtonsElevation() = getDimension(
    R.styleable.VKIDOneTap_vkid_buttonsElevation,
    OneTapButtonElevationStyle.Default.elevationDp.toFloat()
)

@Suppress("MagicNumber")
private fun TypedArray.getOneTapStyleConstructor() = when (getInt(R.styleable.VKIDOneTap_vkid_onetapStyle, 0)) {
    1 -> OneTapStyle::Dark
    2 -> OneTapStyle::TransparentLight
    3 -> OneTapStyle::TransparentDark
    4 -> OneTapStyle::Icon
    else -> OneTapStyle::Light
}

@Suppress("MagicNumber")
private fun TypedArray.getSheetStyleConstructor() = when (getInt(R.styleable.VKIDOneTap_vkid_bottomSheetStyle, 0)) {
    1 -> OneTapBottomSheetStyle::Dark
    2 -> OneTapBottomSheetStyle::TransparentLight
    3 -> OneTapBottomSheetStyle::TransparentDark
    else -> OneTapBottomSheetStyle::Light
}

@Suppress("MagicNumber", "CyclomaticComplexMethod")
private fun TypedArray.getOneTapButtonsSize() = when (getInt(R.styleable.VKIDOneTap_vkid_buttonsSize, 0)) {
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
    return getBoolean(R.styleable.VKIDOneTap_vkid_onetap_show_sign_in_to_another_account, false)
}

private fun TypedArray.getSheetServiceName(): String {
    return getString(R.styleable.VKIDOneTap_vkid_bottomSheetServiceName) ?: ""
}

@Suppress("MagicNumber")
private fun TypedArray.getSheetScenario() = when (getInt(R.styleable.VKIDOneTap_vkid_bottomSheetScenario, 0)) {
    1 -> OneTapScenario.RegistrationForTheEvent
    2 -> OneTapScenario.Application
    3 -> OneTapScenario.OrderInService
    4 -> OneTapScenario.Order
    5 -> OneTapScenario.EnterToAccount
    else -> OneTapScenario.EnterService
}

private fun Context.pixelsToDp(
    px: Float
) = px / (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

private fun Context.dpToPixels(
    dp: Float
) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
