package com.vk.id

/**
 * Represents the failure cases for fetching user data in VK ID.
 *
 * @since 0.0.1
 */
public sealed class VKIDFetchUserFail(
    /**
     * Text description of the failure.
     *
     * @since 0.0.1
     */
    public val description: String
)
