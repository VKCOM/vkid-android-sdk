package com.vk.id.auth

/**
 * Prompt from specification https://openid.net/specs/openid-connect-core-1_0.html#AuthorizationEndpoint
 * Detailed description is in the task ECB-3939
 *
 * @since 2.0.0-alpha
 */
public enum class Prompt {
    /**
     * If logged in authenticate automatically, otherwise show login form
     *
     * @since 2.0.0-alpha
     */
    BLANK,

    /**
     * Always show login form
     *
     * @since 2.0.0-alpha
     */
    LOGIN,

    /**
     * Always show consent screen
     *
     * @since 2.0.0-alpha03
     */
    CONSENT,
}
