package com.vk.id.multibranding.common.callback

import com.vk.id.AccessToken
import com.vk.id.OAuth

/**
 * Represents a callback that will be received after auth with one of multibranding's OAuths.
 */
public sealed interface OAuthListWidgetAuthCallback {

    /**
     * A callback the will be invoked upon a successful auth that provides both [OAuth] and [AccessToken].
     *
     * The first parameter is the [OAuth] that the used authorized with.
     * The second parameter is the [AccessToken] that was received as a result of successful auth.
     */
    public fun interface WithOAuth : (OAuth, AccessToken) -> Unit, OAuthListWidgetAuthCallback

    /**
     * A callback the will be invoked upon a successful auth that provides an [AccessToken].
     *
     * The callback's parameter is the [AccessToken] that was received as a result of successful auth.
     */
    public fun interface JustToken : (AccessToken) -> Unit, OAuthListWidgetAuthCallback
}
