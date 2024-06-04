package com.vk.id.auth

/**
 * Prompt from specification https://openid.net/specs/openid-connect-core-1_0.html#AuthorizationEndpoint
 * Detailed description is in the task ECB-3939
 */
public enum class Prompt {
    /** If logged in authenticate automatically, otherwise show login form */
    BLANK,

    /** Always show login form */
    LOGIN,

    /** Always show consent screen */
    CONSENT,
}
