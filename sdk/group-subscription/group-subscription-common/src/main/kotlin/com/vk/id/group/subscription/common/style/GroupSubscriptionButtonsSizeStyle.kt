package com.vk.id.group.subscription.common.style

import com.vk.id.common.InternalVKIDApi

/**
 * Group subscription sheet's button size style.
 */
@Suppress("MagicNumber")
public enum class GroupSubscriptionButtonsSizeStyle(
    @InternalVKIDApi public val heightDp: Int,
    @InternalVKIDApi public val textSizeSp: Int,
) {
    /**
     * The recommended style.
     */
    DEFAULT(44, 14),

    /**
     * Represents a button with 32dp height.
     */
    SMALL_32(32, 14),

    /**
     * Represents a button with 34dp height.
     */
    SMALL_34(34, 14),

    /**
     * Represents a button with 36dp height.
     */
    SMALL_36(36, 14),

    /**
     * Represents a button with 38dp height.
     */
    SMALL_38(38, 14),

    /**
     * Represents a button with 40dp height.
     */
    MEDIUM_40(40, 16),

    /**
     * Represents a button with 42dp height.
     */
    MEDIUM_42(42, 16),

    /**
     * Represents a button with 44dp height.
     */
    MEDIUM_44(44, 16),

    /**
     * Represents a button with 46dp height.
     */
    MEDIUM_46(46, 16),

    /**
     * Represents a button with 48dp height.
     */
    LARGE_48(48, 18),

    /**
     * Represents a button with 50dp height.
     */
    LARGE_50(50, 17),

    /**
     * Represents a button with 52dp height.
     */
    LARGE_52(52, 17),

    /**
     * Represents a button with 54dp height.
     */
    LARGE_54(54, 17),

    /**
     * Represents a button with 56dp height.
     */
    LARGE_56(56, 17),
}
