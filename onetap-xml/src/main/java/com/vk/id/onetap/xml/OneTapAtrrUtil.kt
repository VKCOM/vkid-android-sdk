package com.vk.id.onetap.xml

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonElevationStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle

internal fun parseAttrs(
    context: Context,
    attrs: AttributeSet?,
): Pair<OneTapStyle, Boolean> {
    context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.VKIDButton,
        0,
        0
    ).apply {
        try {
            return getStyleConstructor()(
                OneTapButtonCornersStyle.Custom(getCornerRadius().toInt()),
                getSize(),
                OneTapButtonElevationStyle.Custom(getElevation().toInt())
            ) to getSignInToAnotherAccountButtonEnabled()
        } finally {
            recycle()
        }
    }
}

private fun TypedArray.getCornerRadius() = getDimension(
    R.styleable.VKIDButton_vkid_cornerRadius,
    OneTapButtonCornersStyle.Default.radiusDp.toFloat()
)

private fun TypedArray.getElevation() = getDimension(
    R.styleable.VKIDButton_vkid_elevation,
    OneTapButtonElevationStyle.Default.elevation.toFloat()
)

@Suppress("MagicNumber")
private fun TypedArray.getStyleConstructor() = when (getInt(R.styleable.VKIDButton_vkid_style, 0)) {
    1 -> OneTapStyle::Dark
    2 -> OneTapStyle::TransparentLight
    3 -> OneTapStyle::TransparentDark
    4 -> OneTapStyle::Icon
    else -> OneTapStyle::Light
}

@Suppress("MagicNumber", "CyclomaticComplexMethod")
private fun TypedArray.getSize() = when (getInt(R.styleable.VKIDButton_vkid_size, 0)) {
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
    return getBoolean(R.styleable.VKIDButton_vkid_show_sign_in_to_another_account, false)
}
