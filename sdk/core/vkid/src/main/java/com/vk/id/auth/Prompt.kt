package com.vk.id.auth

import com.vk.id.common.InternalVKIDApi

/**
 * Prompt from specification https://openid.net/specs/openid-connect-core-1_0.html#AuthorizationEndpoint
 * Detailed description is in the task ECB-3939
 */
@InternalVKIDApi
public enum class Prompt {
    /** If logged in authenticate automatically, otherwise show login form */
    BLANK,

    /** Always show login form */
    LOGIN,
}
