@file:OptIn(InternalVKIDApi::class)

package com.vk.id.multibranding

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.vk.id.common.InternalVKIDApi
import com.vk.id.multibranding.common.style.InternalVKIDOAuthListWidgetBorderStyle
import com.vk.id.multibranding.common.style.OAuthListWidgetCornersStyle

@OptIn(InternalVKIDApi::class)
@Suppress("ModifierComposed")
internal fun Modifier.border(
    style: InternalVKIDOAuthListWidgetBorderStyle,
    cornersStyle: OAuthListWidgetCornersStyle,
) = composed {
    when (style) {
        InternalVKIDOAuthListWidgetBorderStyle.DARK -> border(
            width = 1.dp,
            color = colorResource(id = R.color.vkid_black_alpha12),
            shape = RoundedCornerShape(size = cornersStyle.radiusDp.dp)
        )
        InternalVKIDOAuthListWidgetBorderStyle.LIGHT -> border(
            width = 1.dp,
            color = colorResource(id = R.color.vkid_white_alpha12),
            shape = RoundedCornerShape(size = cornersStyle.radiusDp.dp)
        )
    }
}
