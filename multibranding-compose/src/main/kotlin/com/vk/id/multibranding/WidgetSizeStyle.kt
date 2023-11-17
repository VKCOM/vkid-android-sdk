package com.vk.id.multibranding

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

public enum class WidgetSizeStyle {
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
public fun Modifier.iconPadding(
    style: WidgetSizeStyle
): Modifier = padding(
    style.iconPadding()
)

@Suppress("CyclomaticComplexMethod", "MagicNumber")
public fun WidgetSizeStyle.iconPadding(): Dp = when (this) {
    WidgetSizeStyle.DEFAULT -> 8
    WidgetSizeStyle.SMALL_32 -> 4
    WidgetSizeStyle.SMALL_34 -> 5
    WidgetSizeStyle.SMALL_36 -> 6
    WidgetSizeStyle.SMALL_38 -> 7
    WidgetSizeStyle.MEDIUM_40 -> 6
    WidgetSizeStyle.MEDIUM_42 -> 7
    WidgetSizeStyle.MEDIUM_44 -> 8
    WidgetSizeStyle.MEDIUM_46 -> 9
    WidgetSizeStyle.LARGE_48 -> 10
    WidgetSizeStyle.LARGE_50 -> 11
    WidgetSizeStyle.LARGE_52 -> 12
    WidgetSizeStyle.LARGE_54 -> 13
    WidgetSizeStyle.LARGE_56 -> 14
}.dp

@Suppress("MagicNumber")
public fun WidgetSizeStyle.iconSize(): Dp = when (this) {
    WidgetSizeStyle.DEFAULT -> 28
    WidgetSizeStyle.SMALL_32,
    WidgetSizeStyle.SMALL_34,
    WidgetSizeStyle.SMALL_36,
    WidgetSizeStyle.SMALL_38 -> 24
    WidgetSizeStyle.MEDIUM_40,
    WidgetSizeStyle.MEDIUM_42,
    WidgetSizeStyle.MEDIUM_44,
    WidgetSizeStyle.MEDIUM_46,
    WidgetSizeStyle.LARGE_48,
    WidgetSizeStyle.LARGE_50,
    WidgetSizeStyle.LARGE_52,
    WidgetSizeStyle.LARGE_54,
    WidgetSizeStyle.LARGE_56 -> 28
}.dp

public fun Modifier.height(style: WidgetSizeStyle): Modifier {
    return height(style.heightDp().dp)
}

@Suppress("CyclomaticComplexMethod", "MagicNumber")
public fun WidgetSizeStyle.heightDp(): Int = when (this) {
    WidgetSizeStyle.DEFAULT -> 44
    WidgetSizeStyle.SMALL_32 -> 32
    WidgetSizeStyle.SMALL_34 -> 34
    WidgetSizeStyle.SMALL_36 -> 36
    WidgetSizeStyle.SMALL_38 -> 38
    WidgetSizeStyle.MEDIUM_40 -> 40
    WidgetSizeStyle.MEDIUM_42 -> 42
    WidgetSizeStyle.MEDIUM_44 -> 44
    WidgetSizeStyle.MEDIUM_46 -> 46
    WidgetSizeStyle.LARGE_48 -> 48
    WidgetSizeStyle.LARGE_50 -> 50
    WidgetSizeStyle.LARGE_52 -> 52
    WidgetSizeStyle.LARGE_54 -> 54
    WidgetSizeStyle.LARGE_56 -> 56
}

@Suppress("CyclomaticComplexMethod", "MagicNumber")
public fun WidgetSizeStyle.asFontSize(): TextUnit = when (this) {
    WidgetSizeStyle.SMALL_32,
    WidgetSizeStyle.SMALL_34,
    WidgetSizeStyle.SMALL_36,
    WidgetSizeStyle.SMALL_38 -> 14

    WidgetSizeStyle.DEFAULT,
    WidgetSizeStyle.MEDIUM_40,
    WidgetSizeStyle.MEDIUM_42,
    WidgetSizeStyle.MEDIUM_44,
    WidgetSizeStyle.MEDIUM_46 -> 16

    WidgetSizeStyle.LARGE_48,
    WidgetSizeStyle.LARGE_50,
    WidgetSizeStyle.LARGE_52,
    WidgetSizeStyle.LARGE_54,
    WidgetSizeStyle.LARGE_56 -> 17
}.sp

@Suppress("CyclomaticComplexMethod", "MagicNumber")
public fun WidgetSizeStyle.asLineHeight(): TextUnit = when (this) {
    WidgetSizeStyle.SMALL_32,
    WidgetSizeStyle.SMALL_34,
    WidgetSizeStyle.SMALL_36,
    WidgetSizeStyle.SMALL_38 -> 18

    WidgetSizeStyle.DEFAULT,
    WidgetSizeStyle.MEDIUM_40,
    WidgetSizeStyle.MEDIUM_42,
    WidgetSizeStyle.MEDIUM_44,
    WidgetSizeStyle.MEDIUM_46 -> 20

    WidgetSizeStyle.LARGE_48,
    WidgetSizeStyle.LARGE_50,
    WidgetSizeStyle.LARGE_52,
    WidgetSizeStyle.LARGE_54,
    WidgetSizeStyle.LARGE_56 -> 22
}.sp
