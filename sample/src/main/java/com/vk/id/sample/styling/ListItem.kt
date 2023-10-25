package com.vk.id.sample.styling

import com.vk.id.onetap.compose.button.VKIDButtonStyle

internal sealed class ListItem {

    data class HalfSpacer(
        val isDarkBackground: Boolean = false
    ) : ListItem()

    data class Spacer(
        val isDarkBackground: Boolean = false
    ) : ListItem()

    data class Title(
        val text: String
    ) : ListItem()

    data class Button(
        val style: VKIDButtonStyle,
        val width: Int = 335,
        val isDarkBackground: Boolean = false,
    ) : ListItem()

    data class SmallButton(
        val style: VKIDButtonStyle
    ) : ListItem()
}
