package com.vk.id.onetap.xml

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.common.auth.style.VKIDButtonStyle
import com.vk.id.onetap.common.button.style.OneTapButtonCornersStyle
import com.vk.id.onetap.common.button.style.OneTapButtonElevationStyle
import com.vk.id.onetap.common.button.style.OneTapButtonSizeStyle

// TODO: Rename file
internal fun parseAttrs(
    context: Context,
    attrs: AttributeSet?,
): OneTapStyle {
    context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.VKIDButton,
        0,
        0
    ).apply {
        try {
            return OneTapStyle.Dark(
                // FIXME:
//                getStyleConstructor()(
//                    VKIDButtonCornersStyle.Custom(getCornerRadius().toInt()),
//                    getSize(),
//                    VKIDButtonElevationStyle.Custom(getElevation().toInt())
//                )
            )
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

private fun TypedArray.getStyleConstructor() = when (getInt(R.styleable.VKIDButton_vkid_style, 0)) {
    1 -> VKIDButtonStyle::TransparentLight
    2 -> VKIDButtonStyle::TransparentDark
    else -> VKIDButtonStyle::Blue
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
