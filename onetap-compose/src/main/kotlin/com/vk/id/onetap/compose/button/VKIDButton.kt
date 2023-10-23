package com.vk.id.onetap.compose.button

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.common.button.VKIDButtonStyle
import com.vk.id.onetap.compose.R
import com.vk.id.onetap.compose.icon.VKIcon
import com.vk.id.onetap.compose.progress.CircleProgress
import kotlinx.coroutines.delay

@Composable
public fun VKIDButton(
    modifier: Modifier = Modifier,
    style: VKIDButtonStyle = VKIDButtonStyle.Blue(),
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit = {},
    state: VKIDButtonState = rememberVKIDButtonState()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val vkid = remember { VKID(context) }
    FetchUserData(
        coroutineScope,
        vkid,
        object : OnFetchingProgress {
            override suspend fun onPreFetch() {
                if (state.userIconUrl == null) {
                    state.inProgress = true
                }
            }
            override suspend fun onFetched(newText: String, newIconUrl: String?) {
                if (state.text != newText || state.userIconUrl != newIconUrl) {
                    state.buttonDataVisible = false
                    delay(DURATION_OF_DELAY_BETWEEN_FADE_ANIMATIONS)
                    state.inProgress = false
                    state.text = newText
                    state.userIconUrl = newIconUrl
                    state.buttonDataVisible = true
                } else {
                    state.inProgress = false
                }
            }

            override fun onDispose() {
                state.inProgress = false
            }
        }
    )
    Row(
        modifier = modifier
            .shadow(style.elevationStyle, style.cornersStyle)
            .height(style.sizeStyle)
            .border(style.borderStyle, style.cornersStyle)
            .clip(style.cornersStyle)
            .clipToBounds()
            .background(style.backgroundStyle)
            .clickable(state, coroutineScope, vkid, style, onAuth, onFail),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LeftIconBox(style)
        TextBox(state, style)
        RightIconBox(state, style)
    }
}

@Composable
private fun RowScope.LeftIconBox(
    style: VKIDButtonStyle,
) {
    Box(
        modifier = Modifier
            .iconPadding(style.sizeStyle)
            .weight(1f),
        contentAlignment = Alignment.CenterStart
    ) {
        VKIcon(style = style.iconStyle)
    }
}

@Composable
private fun RowScope.TextBox(
    state: VKIDButtonState,
    style: VKIDButtonStyle
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (state.buttonDataVisible) 1.0f else 0f,
        label = "textAlpha",
        animationSpec = easeInOutAnimation
    )

    @Suppress("MagicNumber")
    Box(
        modifier = Modifier
            .weight(4f)
            .graphicsLayer { this.alpha = animatedAlpha },
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = state.text,
            style = TextStyle(
                color = style.textStyle.asColorResource(),
                fontSize = style.sizeStyle.asFontSize(),
                lineHeight = style.sizeStyle.asLineHeight(),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun RowScope.RightIconBox(
    state: VKIDButtonState,
    style: VKIDButtonStyle
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (state.buttonDataVisible) 1.0f else 0f,
        animationSpec = easeInOutAnimation,
        label = "rightIconAlpha"
    )
    Box(
        modifier = Modifier
            .weight(1f)
            .iconPadding(style.sizeStyle)
            .graphicsLayer { this.alpha = animatedAlpha },
        contentAlignment = Alignment.CenterEnd
    ) {
        if (state.inProgress) {
            CircleProgress(style.progressStyle)
        } else if (state.userIconUrl != null) {
            AsyncImage(
                model = state.userIconUrl,
                contentDescription = null,
                modifier = Modifier.clip(CircleShape),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview
@Composable
private fun PreviewVKIDButton() {
    VKIDButton(onAuth = {})
}

@Preview
@Composable
private fun PreviewVKIDButtonProgress() {
    VKIDButton(
        onAuth = {},
        state = VKIDButtonState(inProgress = true, text = stringResource(R.string.vkid_log_in_with_vkid))
    )
}
