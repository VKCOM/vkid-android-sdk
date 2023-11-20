package com.vk.id.onetap.xml

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.vk.id.onetap.compose.button.VKIDButtonCornersStyle
import com.vk.id.onetap.compose.button.VKIDButtonElevationStyle
import com.vk.id.onetap.compose.button.VKIDButtonSizeStyle
import com.vk.id.onetap.compose.button.VKIDButtonStyle

internal fun parseAttrs(
    context: Context,
    attrs: AttributeSet?,
): VKIDButtonStyle {
    context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.VKIDButton,
        0,
        0
    ).apply {
        try {
            return getStyleConstructor()(
                VKIDButtonCornersStyle.Custom(getCornerRadius().toInt()),
                getSize(),
                VKIDButtonElevationStyle.Custom(getElevation().toInt())
            )
        } finally {
            recycle()
        }
    }
}

private fun TypedArray.getCornerRadius() = getDimension(
    R.styleable.VKIDButton_vkid_cornerRadius,
    VKIDButtonCornersStyle.Default.radiusDp.toFloat()
)

private fun TypedArray.getElevation() = getDimension(
    R.styleable.VKIDButton_vkid_elevation,
    VKIDButtonElevationStyle.Default.elevation.toFloat()
)

private fun TypedArray.getStyleConstructor() = when (getInt(R.styleable.VKIDButton_vkid_style, 0)) {
    1 -> VKIDButtonStyle::TransparentLight
    2 -> VKIDButtonStyle::TransparentDark
    else -> VKIDButtonStyle::Blue
}

@Suppress("MagicNumber", "CyclomaticComplexMethod")
private fun TypedArray.getSize() = when (getInt(R.styleable.VKIDButton_vkid_size, 0)) {
    1 -> VKIDButtonSizeStyle.SMALL_32
    2 -> VKIDButtonSizeStyle.SMALL_34
    3 -> VKIDButtonSizeStyle.SMALL_36
    4 -> VKIDButtonSizeStyle.SMALL_38
    5 -> VKIDButtonSizeStyle.MEDIUM_40
    6 -> VKIDButtonSizeStyle.MEDIUM_42
    7 -> VKIDButtonSizeStyle.MEDIUM_44
    8 -> VKIDButtonSizeStyle.MEDIUM_46
    9 -> VKIDButtonSizeStyle.LARGE_48
    10 -> VKIDButtonSizeStyle.LARGE_50
    11 -> VKIDButtonSizeStyle.LARGE_52
    12 -> VKIDButtonSizeStyle.LARGE_54
    13 -> VKIDButtonSizeStyle.LARGE_56
    else -> VKIDButtonSizeStyle.DEFAULT
}
