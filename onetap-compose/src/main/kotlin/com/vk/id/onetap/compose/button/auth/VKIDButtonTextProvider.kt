package com.vk.id.onetap.compose.button.auth

import com.vk.id.VKIDUser

internal interface VKIDButtonTextProvider {
    fun userFoundText(user: VKIDUser): String
    fun noUserText(): String
}
