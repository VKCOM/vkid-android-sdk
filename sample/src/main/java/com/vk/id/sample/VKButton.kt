package com.vk.id.sample

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VKIDButton(
    modifier: Modifier = Modifier,
    radius: Dp = 8.dp,
    showIcon: Boolean = true,
    inProgress: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = Color.White
                ),
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            )
            .height(44.dp)
            .clipToBounds()
            .clip(RoundedCornerShape(radius))
            .background(colorResource(R.color.vkid_azure_A100)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            if (showIcon) {
                Image(
                    painter = painterResource(id = R.drawable.vkid_icon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colorResource(id = R.color.vkid_white)),
                    modifier = Modifier
                        .size(28.dp)
                        .padding(1.dp)
                )
            }
        }
        Box(
            modifier = Modifier.weight(4f),
            contentAlignment = Alignment.Center
        ) {
            BasicText(
                text = stringResource(R.string.vkid_log_in_with_vkid),
                style = TextStyle(
                    color = colorResource(id = R.color.vkid_white),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (inProgress) {
                CircleProgress()
            }
        }
    }
}

@Composable
fun VKIDButtonSmall(
    onClick: () -> Unit,
    inProgress: Boolean = false,
    enabled: Boolean = true,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(44.dp)
            .height(44.dp)
            .background(color = colorResource(R.color.vkid_azure_A100), shape = RoundedCornerShape(size = 10.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = Color.White
                ),
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            )
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            if (inProgress) {
                CircleProgress()
            } else {
                Image(
                    painter = painterResource(id = R.drawable.vkid_icon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(colorResource(id = R.color.vkid_white)),
                    modifier = Modifier
                        .size(28.dp)
                        .padding(1.dp)
                )
            }
        }
    }
}

@Composable
private fun CircleProgress() {
    val infiniteTransition = rememberInfiniteTransition(label = "vkid_spinner")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "vkid_spinner"
    )
    Image(
        modifier = Modifier
            .graphicsLayer {
                rotationZ = angle
            },
        painter = painterResource(id = R.drawable.vkid_spinner),
        contentDescription = null,
    )
}

@Preview
@Composable
private fun PreviewVKIDButtonSmall() {
    VKIDButtonSmall(onClick = {})
}

@Preview
@Composable
private fun PreviewVKIDButtonSmallProgress() {
    VKIDButtonSmall(onClick = {}, inProgress = true)
}

@Preview
@Composable
private fun PreviewVKIDButton() {
    VKIDButton(onClick = {})
}

@Preview
@Composable
private fun PreviewVKIDButtonNoIcon() {
    VKIDButton(onClick = {}, showIcon = false)
}

@Preview
@Composable
private fun PreviewVKIDButtonProgress() {
    VKIDButton(onClick = {}, inProgress = true)
}
