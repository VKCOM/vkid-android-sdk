package com.vk.id.multibranding

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * [OAuthListWidget]'s size style.
 */
public enum class OAuthListWidgetSizeStyle {
    /** The recommended size */
    DEFAULT,

    /** Style for the widget with 32dp height */
    SMALL_32,

    /** Style for the widget with 34dp height */
    SMALL_34,

    /** Style for the widget with 36dp height */
    SMALL_36,

    /** Style for the widget with 38dp height */
    SMALL_38,

    /** Style for the widget with 40dp height */
    MEDIUM_40,

    /** Style for the widget with 42dp height */
    MEDIUM_42,

    /** Style for the widget with 44dp height */
    MEDIUM_44,

    /** Style for the widget with 46dp height */
    MEDIUM_46,

    /** Style for the widget with 48dp height */
    LARGE_48,

    /** Style for the widget with 50dp height */
    LARGE_50,

    /** Style for the widget with 52dp height */
    LARGE_52,

    /** Style for the widget with 54dp height */
    LARGE_54,

    /** Style for the widget with 56dp height */
    LARGE_56,
}

@Suppress("CyclomaticComplexMethod", "MagicNumber")
internal fun Modifier.iconPadding(
    style: OAuthListWidgetSizeStyle
): Modifier = padding(
    style.iconPadding()
)

@Suppress("CyclomaticComplexMethod", "MagicNumber")
internal fun OAuthListWidgetSizeStyle.iconPadding(): Dp = when (this) {
    OAuthListWidgetSizeStyle.DEFAULT -> 8
    OAuthListWidgetSizeStyle.SMALL_32 -> 4
    OAuthListWidgetSizeStyle.SMALL_34 -> 5
    OAuthListWidgetSizeStyle.SMALL_36 -> 6
    OAuthListWidgetSizeStyle.SMALL_38 -> 7
    OAuthListWidgetSizeStyle.MEDIUM_40 -> 6
    OAuthListWidgetSizeStyle.MEDIUM_42 -> 7
    OAuthListWidgetSizeStyle.MEDIUM_44 -> 8
    OAuthListWidgetSizeStyle.MEDIUM_46 -> 9
    OAuthListWidgetSizeStyle.LARGE_48 -> 10
    OAuthListWidgetSizeStyle.LARGE_50 -> 11
    OAuthListWidgetSizeStyle.LARGE_52 -> 12
    OAuthListWidgetSizeStyle.LARGE_54 -> 13
    OAuthListWidgetSizeStyle.LARGE_56 -> 14
}.dp

@Suppress("MagicNumber")
internal fun OAuthListWidgetSizeStyle.iconSize(): Dp = when (this) {
    OAuthListWidgetSizeStyle.DEFAULT -> 28
    OAuthListWidgetSizeStyle.SMALL_32,
    OAuthListWidgetSizeStyle.SMALL_34,
    OAuthListWidgetSizeStyle.SMALL_36,
    OAuthListWidgetSizeStyle.SMALL_38 -> 24
    OAuthListWidgetSizeStyle.MEDIUM_40,
    OAuthListWidgetSizeStyle.MEDIUM_42,
    OAuthListWidgetSizeStyle.MEDIUM_44,
    OAuthListWidgetSizeStyle.MEDIUM_46,
    OAuthListWidgetSizeStyle.LARGE_48,
    OAuthListWidgetSizeStyle.LARGE_50,
    OAuthListWidgetSizeStyle.LARGE_52,
    OAuthListWidgetSizeStyle.LARGE_54,
    OAuthListWidgetSizeStyle.LARGE_56 -> 28
}.dp

internal fun Modifier.height(style: OAuthListWidgetSizeStyle): Modifier {
    return height(style.heightDp().dp)
}

@Suppress("CyclomaticComplexMethod", "MagicNumber")
internal fun OAuthListWidgetSizeStyle.heightDp(): Int = when (this) {
    OAuthListWidgetSizeStyle.DEFAULT -> 44
    OAuthListWidgetSizeStyle.SMALL_32 -> 32
    OAuthListWidgetSizeStyle.SMALL_34 -> 34
    OAuthListWidgetSizeStyle.SMALL_36 -> 36
    OAuthListWidgetSizeStyle.SMALL_38 -> 38
    OAuthListWidgetSizeStyle.MEDIUM_40 -> 40
    OAuthListWidgetSizeStyle.MEDIUM_42 -> 42
    OAuthListWidgetSizeStyle.MEDIUM_44 -> 44
    OAuthListWidgetSizeStyle.MEDIUM_46 -> 46
    OAuthListWidgetSizeStyle.LARGE_48 -> 48
    OAuthListWidgetSizeStyle.LARGE_50 -> 50
    OAuthListWidgetSizeStyle.LARGE_52 -> 52
    OAuthListWidgetSizeStyle.LARGE_54 -> 54
    OAuthListWidgetSizeStyle.LARGE_56 -> 56
}

@Suppress("CyclomaticComplexMethod", "MagicNumber")
internal fun OAuthListWidgetSizeStyle.asFontSize(): TextUnit = when (this) {
    OAuthListWidgetSizeStyle.SMALL_32,
    OAuthListWidgetSizeStyle.SMALL_34,
    OAuthListWidgetSizeStyle.SMALL_36,
    OAuthListWidgetSizeStyle.SMALL_38 -> 14

    OAuthListWidgetSizeStyle.DEFAULT,
    OAuthListWidgetSizeStyle.MEDIUM_40,
    OAuthListWidgetSizeStyle.MEDIUM_42,
    OAuthListWidgetSizeStyle.MEDIUM_44,
    OAuthListWidgetSizeStyle.MEDIUM_46 -> 16

    OAuthListWidgetSizeStyle.LARGE_48,
    OAuthListWidgetSizeStyle.LARGE_50,
    OAuthListWidgetSizeStyle.LARGE_52,
    OAuthListWidgetSizeStyle.LARGE_54,
    OAuthListWidgetSizeStyle.LARGE_56 -> 17
}.sp

@Suppress("CyclomaticComplexMethod", "MagicNumber")
internal fun OAuthListWidgetSizeStyle.asLineHeight(): TextUnit = when (this) {
    OAuthListWidgetSizeStyle.SMALL_32,
    OAuthListWidgetSizeStyle.SMALL_34,
    OAuthListWidgetSizeStyle.SMALL_36,
    OAuthListWidgetSizeStyle.SMALL_38 -> 18

    OAuthListWidgetSizeStyle.DEFAULT,
    OAuthListWidgetSizeStyle.MEDIUM_40,
    OAuthListWidgetSizeStyle.MEDIUM_42,
    OAuthListWidgetSizeStyle.MEDIUM_44,
    OAuthListWidgetSizeStyle.MEDIUM_46 -> 20

    OAuthListWidgetSizeStyle.LARGE_48,
    OAuthListWidgetSizeStyle.LARGE_50,
    OAuthListWidgetSizeStyle.LARGE_52,
    OAuthListWidgetSizeStyle.LARGE_54,
    OAuthListWidgetSizeStyle.LARGE_56 -> 22
}.sp
