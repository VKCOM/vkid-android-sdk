package com.vk.id

/**
 * Represent an OAuth provider that can be used for authorization.
 */
public enum class OAuth(
    internal val serverName: String
) {
    /**
     * Represents VK OAuth provider.
     */
    VK("vkid"),

    /**
     * Represents Mail OAuth provider.
     */
    MAIL("mail_ru"),

    /**
     * Represents OK OAuth provider.
     */
    OK("ok_ru")
}
