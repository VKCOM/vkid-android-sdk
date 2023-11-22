package com.vk.id.onetap.compose.button

import com.vk.id.VKIDUser

internal interface VKIDButtonTextProvider {
    fun userFoundText(user: VKIDUser): String
    fun noUserText(): String
}
