@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.button.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.DataSource
import com.vk.id.VKID
import com.vk.id.VKIDUser
import com.vk.id.commn.InternalVKIDApi
import com.vk.id.onetap.common.auth.style.VKIDButtonStyle
import com.vk.id.onetap.compose.button.FetchUserData
import com.vk.id.onetap.compose.button.OnFetchingProgress
import com.vk.id.onetap.compose.button.auth.style.background
import com.vk.id.onetap.compose.button.auth.style.border
import com.vk.id.onetap.compose.button.clickable
import com.vk.id.onetap.compose.button.easeInOutAnimation
import com.vk.id.onetap.compose.icon.VKIcon
import com.vk.id.onetap.compose.onetap.style.clip
import com.vk.id.onetap.compose.onetap.style.height
import com.vk.id.onetap.compose.onetap.style.heightDp
import com.vk.id.onetap.compose.onetap.style.shadow
import com.vk.id.onetap.compose.progress.CircleProgress
import kotlinx.coroutines.launch

@Composable
internal fun VKIDButtonSmall(
    state: VKIDSmallButtonState = remember { VKIDSmallButtonState(inProgress = false, userIconLoaded = false) },
    style: VKIDButtonStyle = VKIDButtonStyle.Light(),
    vkid: VKID? = null,
    onClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val useVKID = vkid ?: remember {
        VKID(context)
    }
    FetchUserData(
        coroutineScope,
        useVKID,
        object : OnFetchingProgress {
            override suspend fun onPreFetch() { /*nothing*/
            }

            override fun onDispose() { /*nothing*/
            }

            override suspend fun onFetched(user: VKIDUser?) {
                val newIconUrl = user?.photo200
                if (newIconUrl != null) {
                    state.userIconUrl = newIconUrl
                } else {
                    state.userIconLoaded = false
                }
            }
        }
    )
    var size by remember { mutableStateOf(IntSize.Zero) }
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .shadow(style.elevationStyle, style.cornersStyle)
            .width(style.sizeStyle.heightDp().dp)
            .height(style.sizeStyle)
            .border(style.borderStyle, style.cornersStyle)
            .clip(style.cornersStyle)
            .background(style.backgroundStyle)
            .clickable(style, onClick)
            .onSizeChanged {
                size = it
            },
    ) {
        val animatedOffsetXVkIcon = remember { Animatable(0f) }
        val animatedOffsetXUserIcon = remember { Animatable(0f) }
        SmallButtonAnimation(state, animatedOffsetXVkIcon, animatedOffsetXUserIcon, size)
        SmallButtonContent(state, style, animatedOffsetXVkIcon, animatedOffsetXUserIcon)
    }
}

@Composable
private fun SmallButtonAnimation(
    state: VKIDSmallButtonState,
    animatedOffsetXVkIcon: Animatable<Float, AnimationVector1D>,
    animatedOffsetXUserIcon: Animatable<Float, AnimationVector1D>,
    size: IntSize,
) {
    LaunchedEffect(state.userIconLoaded, state.userIconLoading) {
        if (state.userIconLoaded) {
            launch {
                animatedOffsetXUserIcon.animateTo(
                    targetValue = 0f,
                    animationSpec = easeInOutAnimation
                )
            }
            launch {
                animatedOffsetXVkIcon.animateTo(
                    targetValue = -size.width.toFloat(),
                    animationSpec = easeInOutAnimation
                )
            }
        } else {
            // we here when userIconLoading == true || userIconLoaded = false
            animatedOffsetXUserIcon.snapTo(size.width.toFloat())
            animatedOffsetXVkIcon.snapTo(0f)
        }
    }
}

@Composable
private fun SmallButtonContent(
    state: VKIDSmallButtonState,
    style: VKIDButtonStyle,
    animatedOffsetXVkIcon: Animatable<Float, AnimationVector1D>,
    animatedOffsetXUserIcon: Animatable<Float, AnimationVector1D>
) {
    Box(
        modifier = Modifier
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (state.inProgress) {
            CircleProgress(style.progressStyle)
        } else {
            VKIcon(
                modifier = Modifier
                    .size(28.dp)
                    .graphicsLayer { this.translationX = animatedOffsetXVkIcon.value },
                style = style.iconStyle
            )

            AsyncImage(
                model = state.userIconUrl,
                contentDescription = null,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = animatedOffsetXUserIcon.value
                        clip = true
                        shape = CircleShape
                    },
                contentScale = ContentScale.FillBounds,
                onLoading = {
                    state.userIconLoading = true
                },
                onSuccess = {
                    state.userIconLoading = false
                    if (it.result.dataSource != DataSource.MEMORY_CACHE) {
                        state.userIconLoaded = true
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewVKIDButtonSmall() {
    VKIDButtonSmall(onClick = {})
}

@Preview
@Composable
private fun PreviewVKIDButtonSmallProgress() {
    VKIDButtonSmall(
        onClick = {},
        state = VKIDSmallButtonState(inProgress = true, userIconLoaded = false)
    )
}

@Preview
@Composable
private fun PreviewVKIDButtonSmallUser() {
    VKIDButtonSmall(
        onClick = {},
        state = VKIDSmallButtonState(inProgress = false, userIconLoaded = true)
    )
}

@Preview
@Composable
private fun PreviewVKIDButtonSmallProgressAndUser() {
    VKIDButtonSmall(
        onClick = {},
        state = VKIDSmallButtonState(inProgress = true, userIconLoaded = true)
    )
}
