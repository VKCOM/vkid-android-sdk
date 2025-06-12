package com.vk.id.onetap.common

import com.vk.id.OAuth

/**
 * Represents an OAuth that is displayed on OneTap widgets.
 *
 * @since 1.0.0
 */
public enum class OneTapOAuth {
    /**
     * Represents Mail OAuth provider.
     *
     * @since 1.0.0
     */
    MAIL,

    /**
     * Represents OK OAuth provider.
     *
     * @since 1.0.0
     */
    OK;

    /**
     * Converts this [OneTapOAuth] to corresponding [OAuth].
     *
     * @since 1.0.0
     */
    public fun toOAuth(): OAuth = when (this) {
        MAIL -> OAuth.MAIL
        OK -> OAuth.OK
    }

    /** @suppress */
    public companion object {
        /**
         * Converts [OAuth] to corresponding [OneTapOAuth].
         *
         * @param oAuth An [OAuth] to convert.
         *
         * @since 1.0.0
         */
        public fun fromOAuth(
            oAuth: OAuth
        ): OneTapOAuth? = when (oAuth) {
            OAuth.VK -> null
            OAuth.MAIL -> MAIL
            OAuth.OK -> OK
        }
    }
}
