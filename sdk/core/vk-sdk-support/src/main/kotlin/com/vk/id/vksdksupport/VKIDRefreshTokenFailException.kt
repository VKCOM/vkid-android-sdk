package com.vk.id.vksdksupport

import com.vk.id.refresh.VKIDRefreshTokenFail

/**
 * An exception which is thrown during VK Android SDK Request while using [withVKIDToken] if the the SDK failed to refresh access token.
 *
 * @property fail Fail description.
 */
public class VKIDRefreshTokenFailException(
    public val fail: VKIDRefreshTokenFail
) : IllegalStateException("Failed to refresh access token")
