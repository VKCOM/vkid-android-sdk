package com.vk.id.onetap.compose.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vk.id.onetap.common.icon.VKIconColorStyle
import com.vk.id.onetap.common.icon.VKIconSizeStyle
import com.vk.id.onetap.common.icon.VKIconStyle
import com.vk.id.onetap.compose.R

@Composable
internal fun VKIcon(
    modifier: Modifier = Modifier,
    style: VKIconStyle,
) {
    Image(
        painter = painterResource(id = R.drawable.vkid_icon),
        contentDescription = null,
        colorFilter = style.colorStyle.asColorFilter(),
        modifier = modifier.size(style.sizeStyle).padding(3.dp)
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
