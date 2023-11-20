package com.vk.id.onetap.xml

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.vk.id.multibranding.WidgetCornersStyle
import com.vk.id.multibranding.WidgetSizeStyle
import com.vk.id.onetap.compose.button.VKIDButtonStyle
import com.vk.id.onetap.compose.button.WigetElevationStyle

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
                WidgetCornersStyle.Custom(getCornerRadius().toInt()),
                getSize(),
                WigetElevationStyle.Custom(getElevation().toInt())
            )
        } finally {
            recycle()
        }
    }
}

private fun TypedArray.getCornerRadius() = getDimension(
    R.styleable.VKIDButton_vkid_cornerRadius,
    WidgetCornersStyle.Default.radiusDp.toFloat()
)

private fun TypedArray.getElevation() = getDimension(
    R.styleable.VKIDButton_vkid_elevation,
    WigetElevationStyle.Default.elevation.toFloat()
)

private fun TypedArray.getStyleConstructor() = when (getInt(R.styleable.VKIDButton_vkid_style, 0)) {
    1 -> VKIDButtonStyle::TransparentLight
    2 -> VKIDButtonStyle::TransparentDark
    else -> VKIDButtonStyle::Blue
}

@Suppress("MagicNumber", "CyclomaticComplexMethod")
private fun TypedArray.getSize() = when (getInt(R.styleable.VKIDButton_vkid_size, 0)) {
    1 -> WidgetSizeStyle.SMALL_32
    2 -> WidgetSizeStyle.SMALL_34
    3 -> WidgetSizeStyle.SMALL_36
    4 -> WidgetSizeStyle.SMALL_38
    5 -> WidgetSizeStyle.MEDIUM_40
    6 -> WidgetSizeStyle.MEDIUM_42
    7 -> WidgetSizeStyle.MEDIUM_44
    8 -> WidgetSizeStyle.MEDIUM_46
    9 -> WidgetSizeStyle.LARGE_48
    10 -> WidgetSizeStyle.LARGE_50
    11 -> WidgetSizeStyle.LARGE_52
    12 -> WidgetSizeStyle.LARGE_54
    13 -> WidgetSizeStyle.LARGE_56
    else -> WidgetSizeStyle.DEFAULT
}
