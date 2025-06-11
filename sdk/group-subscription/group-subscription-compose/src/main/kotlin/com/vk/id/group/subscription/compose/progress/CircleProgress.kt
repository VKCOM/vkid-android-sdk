package com.vk.id.group.subscription.compose.progress

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.vk.id.group.subscription.common.style.GroupSubscriptionStyle
import com.vk.id.group.subscription.compose.R

@Composable
internal fun CircleProgress(
    style: GroupSubscriptionStyle,
    label: String
) {
    when (style) {
        is GroupSubscriptionStyle.Light -> CircleProgressWhite(label = label)
        is GroupSubscriptionStyle.Dark -> CircleProgressBlue(label = label)
    }
}

@Composable
private fun CircleProgressWhite(
    label: String,
) {
    CircleProgress(
        label = label,
        progressRes = R.drawable.vkid_sheet_spinner_white
    )
}

@Composable
private fun CircleProgressBlue(
    label: String,
) {
    CircleProgress(
        label = label,
        progressRes = R.drawable.vkid_sheet_spinner
    )
}

@Composable
private fun CircleProgress(
    @DrawableRes progressRes: Int,
    label: String,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "vkid_auth_in_progress_spinner")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = label,
    )
    Image(
        modifier = Modifier.graphicsLayer {
            rotationZ = angle
        },
        painter = painterResource(progressRes),
        contentDescription = null,
    )
}
