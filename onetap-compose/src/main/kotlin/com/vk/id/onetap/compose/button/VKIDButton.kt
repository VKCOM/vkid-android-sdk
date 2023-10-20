package com.vk.id.onetap.compose.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.common.button.VKIDButtonStyle
import com.vk.id.onetap.compose.R
import com.vk.id.onetap.compose.icon.VKIcon
import com.vk.id.onetap.compose.progress.CircleProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
    FetchUserData(state, coroutineScope, vkid)
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
    style: VKIDButtonStyle
) {
    Box(
        modifier = Modifier
            .iconPadding(style.sizeStyle)
            .weight(1f),
        contentAlignment = Alignment.CenterStart
    ) {
        VKIcon(style.iconStyle)
    }
}

@Composable
private fun RowScope.TextBox(
    state: VKIDButtonState,
    style: VKIDButtonStyle
) {
    @Suppress("MagicNumber")
    Box(
        modifier = Modifier.weight(4f),
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
    Box(
        modifier = Modifier
            .weight(1f)
            .iconPadding(style.sizeStyle),
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

internal fun startAuth(
    coroutineScope: CoroutineScope,
    state: VKIDButtonState,
    vkid: VKID,
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit
) {
    coroutineScope.launch {
        state.inProgress = true
        vkid.authorize(object : VKID.AuthCallback {
            override fun onSuccess(accessToken: AccessToken) {
                state.inProgress = false
                onAuth(accessToken)
            }

            override fun onFail(fail: VKIDAuthFail) {
                state.inProgress = false
                onFail(fail)
            }
        })
    }
}

@Suppress("LongParameterList")
internal fun Modifier.clickable(
    state: VKIDButtonState,
    coroutineScope: CoroutineScope,
    vkid: VKID,
    style: VKIDButtonStyle,
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit
): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(
            color = style.rippleStyle.asColor(),
            radius = style.cornersStyle.radiusDp.dp
        ),
        enabled = state.inProgress.not(),
        role = Role.Button,
        onClick = { startAuth(coroutineScope, state, vkid, onAuth, onFail) }
    )
}

@Composable
internal fun FetchUserData(
    state: VKIDButtonState,
    coroutineScope: CoroutineScope,
    vkid: VKID,
) {
    val resources = LocalContext.current.resources
    LaunchedEffect(state) {
        coroutineScope.launch {
            val user = vkid.fetchUserData().getOrNull()
            if (user != null) {
                state.text = resources.getString(R.string.vkid_log_in_as, user.firstName)
                state.userIconUrl = user.photo200
            }
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
