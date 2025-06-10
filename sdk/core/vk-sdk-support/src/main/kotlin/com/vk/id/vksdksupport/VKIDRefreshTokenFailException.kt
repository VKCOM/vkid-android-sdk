package com.vk.id.vksdksupport

import com.vk.id.refresh.VKIDRefreshTokenFail

/**
 * An exception which is thrown during VK Android SDK Request while using [withVKIDToken] if the the SDK failed to refresh access token.
 *
 * @param fail Fail description.
 *
 * @since 1.3.2
 */
public class VKIDRefreshTokenFailException(
    /**
     * Fail description
     *
     * @since 1.3.2
     */
    public val fail: VKIDRefreshTokenFail
) : IllegalStateException("Failed to refresh access token")
