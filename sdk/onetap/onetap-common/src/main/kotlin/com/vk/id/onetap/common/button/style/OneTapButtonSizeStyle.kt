@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.common.button.style

import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.icon.style.InternalVKIconSizeStyle

/**
 * OneTap's button size style.
 *
 * @since 1.0.0
 */
public enum class OneTapButtonSizeStyle {
    /**
     * The recommended style.
     *
     * @since 1.0.0
     */
    DEFAULT,

    /**
     * Represents a button with 32dp height.
     *
     * @since 1.0.0
     */
    SMALL_32,

    /**
     * Represents a button with 34dp height.
     *
     * @since 1.0.0
     */
    SMALL_34,

    /**
     * Represents a button with 36dp height.
     *
     * @since 1.0.0
     */
    SMALL_36,

    /**
     * Represents a button with 38dp height.
     *
     * @since 1.0.0
     */
    SMALL_38,

    /**
     * Represents a button with 40dp height.
     *
     * @since 1.0.0
     */
    MEDIUM_40,

    /**
     * Represents a button with 42dp height.
     *
     * @since 1.0.0
     */
    MEDIUM_42,

    /**
     * Represents a button with 44dp height.
     *
     * @since 1.0.0
     */
    MEDIUM_44,

    /**
     * Represents a button with 46dp height.
     *
     * @since 1.0.0
     */
    MEDIUM_46,

    /**
     * Represents a button with 48dp height.
     *
     * @since 1.0.0
     */
    LARGE_48,

    /**
     * Represents a button with 50dp height.
     *
     * @since 1.0.0
     */
    LARGE_50,

    /**
     * Represents a button with 52dp height.
     *
     * @since 1.0.0
     */
    LARGE_52,

    /**
     * Represents a button with 54dp height.
     *
     * @since 1.0.0
     */
    LARGE_54,

    /**
     * Represents a button with 56dp height.
     *
     * @since 1.0.0
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
