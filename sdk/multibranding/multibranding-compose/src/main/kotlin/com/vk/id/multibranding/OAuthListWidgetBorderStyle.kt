@file:OptIn(InternalVKIDApi::class)

package com.vk.id.multibranding

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.vk.id.common.InternalVKIDApi
import com.vk.id.multibranding.common.style.OAuthListWidgetCornersStyle
import com.vk.id.multibranding.common.style.VKIDOAuthListWidgetBorderStyle

@OptIn(InternalVKIDApi::class)
internal fun Modifier.border(
    style: VKIDOAuthListWidgetBorderStyle,
    cornersStyle: OAuthListWidgetCornersStyle,
) = composed {
    when (style) {
        VKIDOAuthListWidgetBorderStyle.DARK -> border(
            width = 1.dp,
            color = colorResource(id = R.color.vkid_black_alpha12),
            shape = RoundedCornerShape(size = cornersStyle.radiusDp.dp)
        )
        VKIDOAuthListWidgetBorderStyle.LIGHT -> border(
            width = 1.dp,
            color = colorResource(id = R.color.vkid_white_alpha12),
            shape = RoundedCornerShape(size = cornersStyle.radiusDp.dp)
        )
    }
}
