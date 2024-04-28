@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.icon

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.icon.style.InternalVKIconColorStyle
import com.vk.id.onetap.common.icon.style.InternalVKIconSizeStyle
import com.vk.id.onetap.common.icon.style.InternalVKIconStyle

@Composable
internal fun VKIcon(
    modifier: Modifier = Modifier,
    style: InternalVKIconStyle,
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
    style = InternalVKIconStyle(
        colorStyle = InternalVKIconColorStyle.BLUE,
        sizeStyle = InternalVKIconSizeStyle.NORMAL,
    )
)

@Preview
@Composable
private fun VKIconWhite() = VKIcon(
    style = InternalVKIconStyle(
        colorStyle = InternalVKIconColorStyle.WHITE,
        sizeStyle = InternalVKIconSizeStyle.SMALL,
    )
)
