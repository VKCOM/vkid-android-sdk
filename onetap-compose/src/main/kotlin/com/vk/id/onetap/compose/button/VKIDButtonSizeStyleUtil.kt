package com.vk.id.onetap.compose.button

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk.id.onetap.common.button.VKIDButtonSizeStyle

@Suppress("CyclomaticComplexMethod", "MagicNumber")
internal fun Modifier.iconPadding(
    style: VKIDButtonSizeStyle
) = padding(
    when (style) {
        VKIDButtonSizeStyle.DEFAULT -> 8
        VKIDButtonSizeStyle.SMALL_32 -> 4
        VKIDButtonSizeStyle.SMALL_34 -> 5
        VKIDButtonSizeStyle.SMALL_36 -> 6
        VKIDButtonSizeStyle.SMALL_38 -> 7
        VKIDButtonSizeStyle.MEDIUM_40 -> 6
        VKIDButtonSizeStyle.MEDIUM_42 -> 7
        VKIDButtonSizeStyle.MEDIUM_44 -> 8
        VKIDButtonSizeStyle.MEDIUM_46 -> 9
        VKIDButtonSizeStyle.LARGE_48 -> 10
        VKIDButtonSizeStyle.LARGE_50 -> 11
        VKIDButtonSizeStyle.LARGE_52 -> 12
        VKIDButtonSizeStyle.LARGE_54 -> 13
        VKIDButtonSizeStyle.LARGE_56 -> 14
    }.dp
)

internal fun Modifier.height(style: VKIDButtonSizeStyle): Modifier {
    return height(style.heightDp().dp)
}

@Suppress("CyclomaticComplexMethod", "MagicNumber")
internal fun VKIDButtonSizeStyle.heightDp() = when (this) {
    VKIDButtonSizeStyle.DEFAULT -> 44
    VKIDButtonSizeStyle.SMALL_32 -> 32
    VKIDButtonSizeStyle.SMALL_34 -> 34
    VKIDButtonSizeStyle.SMALL_36 -> 36
    VKIDButtonSizeStyle.SMALL_38 -> 38
    VKIDButtonSizeStyle.MEDIUM_40 -> 40
    VKIDButtonSizeStyle.MEDIUM_42 -> 42
    VKIDButtonSizeStyle.MEDIUM_44 -> 44
    VKIDButtonSizeStyle.MEDIUM_46 -> 46
    VKIDButtonSizeStyle.LARGE_48 -> 48
    VKIDButtonSizeStyle.LARGE_50 -> 50
    VKIDButtonSizeStyle.LARGE_52 -> 52
    VKIDButtonSizeStyle.LARGE_54 -> 54
    VKIDButtonSizeStyle.LARGE_56 -> 56
}

@Suppress("CyclomaticComplexMethod", "MagicNumber")
internal fun VKIDButtonSizeStyle.asFontSize() = when (this) {
    VKIDButtonSizeStyle.SMALL_32,
    VKIDButtonSizeStyle.SMALL_34,
    VKIDButtonSizeStyle.SMALL_36,
    VKIDButtonSizeStyle.SMALL_38 -> 14

    VKIDButtonSizeStyle.DEFAULT,
    VKIDButtonSizeStyle.MEDIUM_40,
    VKIDButtonSizeStyle.MEDIUM_42,
    VKIDButtonSizeStyle.MEDIUM_44,
    VKIDButtonSizeStyle.MEDIUM_46 -> 16

    VKIDButtonSizeStyle.LARGE_48,
    VKIDButtonSizeStyle.LARGE_50,
    VKIDButtonSizeStyle.LARGE_52,
    VKIDButtonSizeStyle.LARGE_54,
    VKIDButtonSizeStyle.LARGE_56 -> 17
}.sp

@Suppress("CyclomaticComplexMethod", "MagicNumber")
internal fun VKIDButtonSizeStyle.asLineHeight() = when (this) {
    VKIDButtonSizeStyle.SMALL_32,
    VKIDButtonSizeStyle.SMALL_34,
    VKIDButtonSizeStyle.SMALL_36,
    VKIDButtonSizeStyle.SMALL_38 -> 18

    VKIDButtonSizeStyle.DEFAULT,
    VKIDButtonSizeStyle.MEDIUM_40,
    VKIDButtonSizeStyle.MEDIUM_42,
    VKIDButtonSizeStyle.MEDIUM_44,
    VKIDButtonSizeStyle.MEDIUM_46 -> 20

    VKIDButtonSizeStyle.LARGE_48,
    VKIDButtonSizeStyle.LARGE_50,
    VKIDButtonSizeStyle.LARGE_52,
    VKIDButtonSizeStyle.LARGE_54,
    VKIDButtonSizeStyle.LARGE_56 -> 22
}.sp
