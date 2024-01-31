package com.vk.id.onetap.common

import com.vk.id.OAuth

/**
 * Represents an OAuth that is displayed on OneTap widgets.
 */
public enum class OneTapOAuth {
    /**
     * Represents Mail OAuth provider.
     */
    MAIL,

    /**
     * Represents OK OAuth provider.
     */
    OK;

    /**
     * Converts this [OneTapOAuth] to corresponding [OAuth].
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
