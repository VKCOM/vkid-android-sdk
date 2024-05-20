@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.common.button.style

import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.icon.style.InternalVKIconSizeStyle

/**
 * OneTap's button size style.
 */
public enum class OneTapButtonSizeStyle {
    /**
     * The recommended style.
     */
    DEFAULT,

    /**
     * Represents a button with 32dp height.
     */
    SMALL_32,

    /**
     * Represents a button with 34dp height.
     */
    SMALL_34,

    /**
     * Represents a button with 36dp height.
     */
    SMALL_36,

    /**
     * Represents a button with 38dp height.
     */
    SMALL_38,

    /**
     * Represents a button with 40dp height.
     */
    MEDIUM_40,

    /**
     * Represents a button with 42dp height.
     */
    MEDIUM_42,

    /**
     * Represents a button with 44dp height.
     */
    MEDIUM_44,

    /**
     * Represents a button with 46dp height.
     */
    MEDIUM_46,

    /**
     * Represents a button with 48dp height.
     */
    LARGE_48,

    /**
     * Represents a button with 50dp height.
     */
    LARGE_50,

    /**
     * Represents a button with 52dp height.
     */
    LARGE_52,

    /**
     * Represents a button with 54dp height.
     */
    LARGE_54,

    /**
     * Represents a button with 56dp height.
     */
    LARGE_56,
}

@InternalVKIDApi
public fun OneTapButtonSizeStyle.internalVKIDAsIconSizeStyle(): InternalVKIconSizeStyle = when (this) {
    OneTapButtonSizeStyle.DEFAULT -> InternalVKIconSizeStyle.NORMAL
    OneTapButtonSizeStyle.SMALL_32,
    OneTapButtonSizeStyle.SMALL_34,
    OneTapButtonSizeStyle.SMALL_36,
    OneTapButtonSizeStyle.SMALL_38 -> InternalVKIconSizeStyle.SMALL

    OneTapButtonSizeStyle.MEDIUM_40,
    OneTapButtonSizeStyle.MEDIUM_42,
    OneTapButtonSizeStyle.MEDIUM_44,
    OneTapButtonSizeStyle.MEDIUM_46,
    OneTapButtonSizeStyle.LARGE_48,
    OneTapButtonSizeStyle.LARGE_50,
    OneTapButtonSizeStyle.LARGE_52,
    OneTapButtonSizeStyle.LARGE_54,
    OneTapButtonSizeStyle.LARGE_56 -> InternalVKIconSizeStyle.NORMAL
}
