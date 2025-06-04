package com.vk.id

/**
 * Represent an OAuth provider that can be used for authorization.
 *
 * @since 1.0.0
 */
public enum class OAuth(
    internal val serverName: String
) {
    /**
     * Represents VK OAuth provider.
     *
     * @since 1.0.0
     */
    VK("vkid"),

    /**
     * Represents Mail OAuth provider.
     *
     * @since 1.0.0
     */
    MAIL("mail_ru"),

    /**
     * Represents OK OAuth provider.
     *
     * @since 1.0.0
     */
    OK("ok_ru")
}
