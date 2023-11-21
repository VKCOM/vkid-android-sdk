package com.vk.id.internal.auth

public enum class OAuth(
    internal val serverName: String
) {
    VK("vk_id"), MAIL("mail_ru"), OK("ok_ru")
}
