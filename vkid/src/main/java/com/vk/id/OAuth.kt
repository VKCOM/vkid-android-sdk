package com.vk.id

/**
 * Represent an OAuth provider that can be used for authorization.
 */
public enum class OAuth(
    internal val serverName: String
) {
    VK("vk_id"), MAIL("mail_ru"), OK("ok_ru")
}
