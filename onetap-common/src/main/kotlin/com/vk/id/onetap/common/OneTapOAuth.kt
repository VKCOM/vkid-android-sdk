package com.vk.id.onetap.common

import com.vk.id.OAuth

/**
 * Represents an OAuth that is displayed on OneTap widgets
 */
public enum class OneTapOAuth {
    MAIL, OK;

    public fun toOAuth(): OAuth = when (this) {
        MAIL -> OAuth.MAIL
        OK -> OAuth.OK
    }

    public companion object {
        public fun fromOAuth(
            oAuth: OAuth
        ): OneTapOAuth? = when (oAuth) {
            OAuth.VK -> null
            OAuth.MAIL -> MAIL
            OAuth.OK -> OK
        }
    }
}
