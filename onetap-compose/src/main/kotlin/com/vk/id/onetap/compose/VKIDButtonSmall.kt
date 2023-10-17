package com.vk.id.onetap.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail

@Composable
public fun VKIDButtonSmall(
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit = {},
    state: VKIDButtonState = rememberVKIDButtonState()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val vkid: VKID = remember {
        VKID(context)
    }
    FetchUserData(state, coroutineScope, vkid)
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(44.dp)
            .height(44.dp)
            .background(
                color = colorResource(R.color.vkid_azure_A100),
                shape = RoundedCornerShape(size = 10.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = Color.White
                ),
                enabled = state.inProgress.not(),
                role = Role.Button,
                onClick = {
                    startAuth(coroutineScope, state, vkid, onAuth, onFail)
                }
            )
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            if (state.inProgress) {
                CircleProgress()
            } else if (state.userIconUrl != null) {
                AsyncImage(
                    model = state.userIconUrl,
                    contentDescription = null,
                    modifier = Modifier.clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
            } else {
                VKIcon()
            }
        }
    }
}

@Preview
@Composable
private fun PreviewVKIDButtonSmall() {
    VKIDButtonSmall(onAuth = {})
}

@Preview
@Composable
private fun PreviewVKIDButtonSmallProgress() {
    VKIDButtonSmall(
        onAuth = {},
        state = VKIDButtonState(inProgress = true, text = stringResource(R.string.vkid_log_in_with_vkid))
    )
}
