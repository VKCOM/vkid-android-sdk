@file:OptIn(InternalVKIDApi::class)

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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.progress.style.CircleProgressStyle
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
            .semantics {
                @OptIn(ExperimentalComposeUiApi::class)
                testTagsAsResourceId = true
            }
            .testTag("circle_progress")
            .graphicsLayer {
                rotationZ = angle
            },
        painter = painterResource(id = R.drawable.vkid_spinner),
        contentDescription = null,
        colorFilter = style.asColorFilter(),
    )
}
