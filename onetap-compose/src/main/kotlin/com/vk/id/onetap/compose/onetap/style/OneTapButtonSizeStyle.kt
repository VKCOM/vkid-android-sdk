package com.vk.id.onetap.compose.onetap.style

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

public enum class OneTapButtonSizeStyle {
    DEFAULT,

    SMALL_32,
    SMALL_34,
    SMALL_36,
    SMALL_38,

    MEDIUM_40,
    MEDIUM_42,
    MEDIUM_44,
    MEDIUM_46,

    LARGE_48,
    LARGE_50,
    LARGE_52,
    LARGE_54,
    LARGE_56,
}

@Suppress("CyclomaticComplexMethod", "MagicNumber")
internal fun Modifier.iconPadding(
    style: OneTapButtonSizeStyle
) = padding(
    style.iconPadding()
)

@Suppress("CyclomaticComplexMethod", "MagicNumber")
internal fun OneTapButtonSizeStyle.iconPadding() = when (this) {
    OneTapButtonSizeStyle.DEFAULT -> 8
    OneTapButtonSizeStyle.SMALL_32 -> 4
    OneTapButtonSizeStyle.SMALL_34 -> 5
    OneTapButtonSizeStyle.SMALL_36 -> 6
    OneTapButtonSizeStyle.SMALL_38 -> 7
    OneTapButtonSizeStyle.MEDIUM_40 -> 6
    OneTapButtonSizeStyle.MEDIUM_42 -> 7
    OneTapButtonSizeStyle.MEDIUM_44 -> 8
    OneTapButtonSizeStyle.MEDIUM_46 -> 9
    OneTapButtonSizeStyle.LARGE_48 -> 10
    OneTapButtonSizeStyle.LARGE_50 -> 11
    OneTapButtonSizeStyle.LARGE_52 -> 12
    OneTapButtonSizeStyle.LARGE_54 -> 13
    OneTapButtonSizeStyle.LARGE_56 -> 14
}.dp

internal fun Modifier.height(style: OneTapButtonSizeStyle): Modifier {
    return height(style.heightDp().dp)
}

@Suppress("CyclomaticComplexMethod", "MagicNumber")
internal fun OneTapButtonSizeStyle.heightDp() = when (this) {
    OneTapButtonSizeStyle.DEFAULT -> 44
    OneTapButtonSizeStyle.SMALL_32 -> 32
    OneTapButtonSizeStyle.SMALL_34 -> 34
    OneTapButtonSizeStyle.SMALL_36 -> 36
    OneTapButtonSizeStyle.SMALL_38 -> 38
    OneTapButtonSizeStyle.MEDIUM_40 -> 40
    OneTapButtonSizeStyle.MEDIUM_42 -> 42
    OneTapButtonSizeStyle.MEDIUM_44 -> 44
    OneTapButtonSizeStyle.MEDIUM_46 -> 46
    OneTapButtonSizeStyle.LARGE_48 -> 48
    OneTapButtonSizeStyle.LARGE_50 -> 50
    OneTapButtonSizeStyle.LARGE_52 -> 52
    OneTapButtonSizeStyle.LARGE_54 -> 54
    OneTapButtonSizeStyle.LARGE_56 -> 56
}

@Suppress("CyclomaticComplexMethod", "MagicNumber")
internal fun OneTapButtonSizeStyle.asFontSize() = when (this) {
    OneTapButtonSizeStyle.SMALL_32,
    OneTapButtonSizeStyle.SMALL_34,
    OneTapButtonSizeStyle.SMALL_36,
    OneTapButtonSizeStyle.SMALL_38 -> 14

    OneTapButtonSizeStyle.DEFAULT,
    OneTapButtonSizeStyle.MEDIUM_40,
    OneTapButtonSizeStyle.MEDIUM_42,
    OneTapButtonSizeStyle.MEDIUM_44,
    OneTapButtonSizeStyle.MEDIUM_46 -> 16

    OneTapButtonSizeStyle.LARGE_48,
    OneTapButtonSizeStyle.LARGE_50,
    OneTapButtonSizeStyle.LARGE_52,
    OneTapButtonSizeStyle.LARGE_54,
    OneTapButtonSizeStyle.LARGE_56 -> 17
}.sp

@Suppress("CyclomaticComplexMethod", "MagicNumber")
internal fun OneTapButtonSizeStyle.asLineHeight() = when (this) {
    OneTapButtonSizeStyle.SMALL_32,
    OneTapButtonSizeStyle.SMALL_34,
    OneTapButtonSizeStyle.SMALL_36,
    OneTapButtonSizeStyle.SMALL_38 -> 18

    OneTapButtonSizeStyle.DEFAULT,
    OneTapButtonSizeStyle.MEDIUM_40,
    OneTapButtonSizeStyle.MEDIUM_42,
    OneTapButtonSizeStyle.MEDIUM_44,
    OneTapButtonSizeStyle.MEDIUM_46 -> 20

    OneTapButtonSizeStyle.LARGE_48,
    OneTapButtonSizeStyle.LARGE_50,
    OneTapButtonSizeStyle.LARGE_52,
    OneTapButtonSizeStyle.LARGE_54,
    OneTapButtonSizeStyle.LARGE_56 -> 22
}.sp
