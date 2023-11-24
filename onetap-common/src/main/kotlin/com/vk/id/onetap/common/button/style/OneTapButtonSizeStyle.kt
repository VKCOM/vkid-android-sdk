package com.vk.id.onetap.common.button.style

import com.vk.id.onetap.common.icon.style.VKIconSizeStyle

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

public fun OneTapButtonSizeStyle.asIconSizeStyle(): VKIconSizeStyle = when (this) {
    OneTapButtonSizeStyle.DEFAULT -> VKIconSizeStyle.SMALL
    OneTapButtonSizeStyle.SMALL_32,
    OneTapButtonSizeStyle.SMALL_34,
    OneTapButtonSizeStyle.SMALL_36,
    OneTapButtonSizeStyle.SMALL_38 -> VKIconSizeStyle.NORMAL
    OneTapButtonSizeStyle.MEDIUM_40,
    OneTapButtonSizeStyle.MEDIUM_42,
    OneTapButtonSizeStyle.MEDIUM_44,
    OneTapButtonSizeStyle.MEDIUM_46,
    OneTapButtonSizeStyle.LARGE_48,
    OneTapButtonSizeStyle.LARGE_50,
    OneTapButtonSizeStyle.LARGE_52,
    OneTapButtonSizeStyle.LARGE_54,
    OneTapButtonSizeStyle.LARGE_56 -> VKIconSizeStyle.NORMAL
}

