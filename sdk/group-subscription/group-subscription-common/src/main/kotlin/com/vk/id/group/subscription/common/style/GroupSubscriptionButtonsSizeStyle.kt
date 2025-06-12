package com.vk.id.group.subscription.common.style

import com.vk.id.common.InternalVKIDApi

/**
 * Group subscription sheet's button size style.
 *
 * @since 2.5.0
 */
@Suppress("MagicNumber")
public enum class GroupSubscriptionButtonsSizeStyle(
    @InternalVKIDApi public val heightDp: Int,
    @InternalVKIDApi public val textSizeSp: Int,
) {
    /**
     * The recommended style.
     *
     * @since 2.5.0
     */
    DEFAULT(44, 14),

    /**
     * Represents a button with 32dp height.
     *
     * @since 2.5.0
     */
    SMALL_32(32, 14),

    /**
     * Represents a button with 34dp height.
     *
     * @since 2.5.0
     */
    SMALL_34(34, 14),

    /**
     * Represents a button with 36dp height.
     *
     * @since 2.5.0
     */
    SMALL_36(36, 14),

    /**
     * Represents a button with 38dp height.
     *
     * @since 2.5.0
     */
    SMALL_38(38, 14),

    /**
     * Represents a button with 40dp height.
     *
     * @since 2.5.0
     */
    MEDIUM_40(40, 16),

    /**
     * Represents a button with 42dp height.
     *
     * @since 2.5.0
     */
    MEDIUM_42(42, 16),

    /**
     * Represents a button with 44dp height.
     *
     * @since 2.5.0
     */
    MEDIUM_44(44, 16),

    /**
     * Represents a button with 46dp height.
     *
     * @since 2.5.0
     */
    MEDIUM_46(46, 16),

    /**
     * Represents a button with 48dp height.
     *
     * @since 2.5.0
     */
    LARGE_48(48, 18),

    /**
     * Represents a button with 50dp height.
     *
     * @since 2.5.0
     */
    LARGE_50(50, 17),

    /**
     * Represents a button with 52dp height.
     *
     * @since 2.5.0
     */
    LARGE_52(52, 17),

    /**
     * Represents a button with 54dp height.
     *
     * @since 2.5.0
     */
    LARGE_54(54, 17),

    /**
     * Represents a button with 56dp height.
     *
     * @since 2.5.0
     */
    LARGE_56(56, 17),
}
