@file:OptIn(InternalVKIDApi::class)

package com.vk.id.group.subscription.compose.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vk.id.common.InternalVKIDApi
import com.vk.id.group.subscription.common.style.GroupSubscriptionStyle
import com.vk.id.group.subscription.compose.R

@Composable
internal fun SecondaryButton(
    style: GroupSubscriptionStyle,
    testTag: String,
    text: String,
    onClick: () -> Unit
) {
    androidx.compose.material3.FilledTonalButton(
        onClick = onClick,
        modifier = Modifier
            .testTag(testTag)
            .fillMaxWidth()
            .height(style.buttonsSizeStyle.heightDp.dp),
        shape = RoundedCornerShape(style.buttonsCornersStyle.radiusDp.dp),
        colors = ButtonDefaults.filledTonalButtonColors().copy(
            containerColor = textSecondaryButtonBackgroundColor(style),
        ),
    ) {
        Text(
            text = text,
            modifier = Modifier,
            style = TextStyle(
                textAlign = TextAlign.Center,
                color = when (style) {
                    is GroupSubscriptionStyle.Light -> colorResource(R.color.vkid_azure_300)
                    is GroupSubscriptionStyle.Dark -> Color.White
                },
                fontSize = style.buttonsSizeStyle.textSizeSp.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}
