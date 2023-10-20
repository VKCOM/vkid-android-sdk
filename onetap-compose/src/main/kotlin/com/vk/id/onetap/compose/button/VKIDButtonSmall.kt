package com.vk.id.onetap.compose.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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

@Composable
public fun VKIDButtonSmall(
    onAuth: (AccessToken) -> Unit,
    style: VKIDButtonStyle = VKIDButtonStyle.Blue(),
    onFail: (VKIDAuthFail) -> Unit = {},
    state: VKIDButtonState = rememberVKIDButtonState()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val vkid = remember { VKID(context) }
    FetchUserData(state, coroutineScope, vkid)
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
            .clickable(state, coroutineScope, vkid, style, onAuth, onFail),
    ) {
        Box(modifier = Modifier.iconPadding(style.sizeStyle)) {
            if (state.inProgress) {
                CircleProgress(style.progressStyle)
            } else if (state.userIconUrl != null) {
                AsyncImage(
                    model = state.userIconUrl,
                    contentDescription = null,
                    modifier = Modifier.clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
            } else {
                VKIcon(style.iconStyle)
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
