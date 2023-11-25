package com.vk.id.sample.xml.onetap.item

import com.vk.id.onetap.common.OneTapStyle

public data class ButtonItem(
    val style: OneTapStyle,
    val width: Int = 335,
    val isDarkBackground: Boolean = false,
)
