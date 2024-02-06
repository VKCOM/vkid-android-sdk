@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.icon

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.icon.style.VKIconColorStyle
import com.vk.id.onetap.common.icon.style.VKIconSizeStyle
import com.vk.id.onetap.common.icon.style.VKIconStyle

@Composable
internal fun VKIcon(
    modifier: Modifier = Modifier,
    style: VKIconStyle,
) {
    Image(
        painter = painterResource(style.colorStyle.asPainterResource()),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = modifier.size(style.sizeStyle)
    )
}

@Preview
@Composable
private fun VKIconBlue() = VKIcon(
    style = VKIconStyle(
        colorStyle = VKIconColorStyle.BLUE,
        sizeStyle = VKIconSizeStyle.NORMAL,
    )
)

@Preview
@Composable
private fun VKIconWhite() = VKIcon(
    style = VKIconStyle(
        colorStyle = VKIconColorStyle.WHITE,
        sizeStyle = VKIconSizeStyle.SMALL,
    )
)
