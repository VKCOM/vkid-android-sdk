package com.vk.id

/**
 * Represents the failure cases for fetching user data in VK ID.
 */
public sealed class VKIDFetchUserFail(
    /**
     * Text description of the failure.
     */
    public val description: String
)
