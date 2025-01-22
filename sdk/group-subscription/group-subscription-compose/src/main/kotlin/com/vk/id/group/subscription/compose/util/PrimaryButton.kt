@file:OptIn(InternalVKIDApi::class)

package com.vk.id.group.subscription.compose.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.vk.id.common.InternalVKIDApi
import com.vk.id.group.subscription.common.style.GroupSubscriptionStyle

@Composable
internal fun PrimaryButton(
    style: GroupSubscriptionStyle,
    testTag: String,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .testTag(testTag)
            .fillMaxWidth()
            .height(style.buttonsSizeStyle.heightDp.dp),
        shape = RoundedCornerShape(style.buttonsCornersStyle.radiusDp.dp),
        colors = ButtonDefaults.buttonColors(containerColor = textPrimaryButtonBackgroundColor(style))
    ) {
        content()
    }
}
