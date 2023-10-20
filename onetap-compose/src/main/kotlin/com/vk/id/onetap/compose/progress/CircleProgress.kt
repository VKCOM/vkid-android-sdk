package com.vk.id.onetap.compose.progress

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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.vk.id.onetap.common.progress.CircleProgressStyle
import com.vk.id.onetap.compose.R

@Composable
internal fun CircleProgress(
    style: CircleProgressStyle
) {
    val infiniteTransition = rememberInfiniteTransition(label = "vkid_spinner")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "vkid_spinner"
    )
    Image(
        modifier = Modifier
            .graphicsLayer {
                rotationZ = angle
            },
        painter = painterResource(id = R.drawable.vkid_spinner),
        contentDescription = null,
        colorFilter = style.asColorFilter(),
    )
}

@Composable
private fun CircleProgressStyle.asColorFilter(): ColorFilter {
    val colorResource = when (this) {
        CircleProgressStyle.LIGHT -> R.color.vkid_white
        CircleProgressStyle.DARK -> R.color.vkid_black
    }
    return ColorFilter.tint(colorResource(id = colorResource))
}
