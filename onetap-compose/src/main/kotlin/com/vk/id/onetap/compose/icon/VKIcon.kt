package com.vk.id.onetap.compose.icon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vk.id.onetap.compose.R

@Composable
internal fun VKIcon(
    style: VKIconStyle
) {
    Image(
        painter = painterResource(id = R.drawable.vkid_icon),
        contentDescription = null,
        colorFilter = style.asColorFilter(),
        modifier = Modifier
            .size(28.dp)
            .padding(1.dp)
    )
}

@Composable
private fun VKIconStyle.asColorFilter(): ColorFilter {
    val colorResource = when (this) {
        VKIconStyle.WHITE -> R.color.vkid_white
        VKIconStyle.BLUE -> R.color.vkid_azure_A100
    }
    return ColorFilter.tint(colorResource(id = colorResource))
}

@Preview
@Composable
private fun VKIconBlue() = VKIcon(VKIconStyle.BLUE)

@Preview
@Composable
private fun VKIconWhite() = VKIcon(VKIconStyle.WHITE)
