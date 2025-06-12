package com.vk.id.group.subscription.compose.close

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import com.vk.id.group.subscription.compose.R

@Composable
internal fun CloseIcon(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .testTag("group_subscription_close")
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        Image(
            painter = painterResource(R.drawable.vkid_group_subscription_close),
            contentDescription = null,
            contentScale = ContentScale.Fit,
        )
    }
}
