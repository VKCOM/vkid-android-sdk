package com.vk.id.onetap.compose.button.alternate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.onetap.compose.R
import com.vk.id.onetap.compose.button.alternate.style.AlternateAccountButtonStyle
import com.vk.id.onetap.compose.button.alternate.style.asColorResource
import com.vk.id.onetap.compose.button.alternate.style.background
import com.vk.id.onetap.compose.button.asColor
import com.vk.id.onetap.compose.button.asColorResource
import com.vk.id.onetap.compose.button.asFontSize
import com.vk.id.onetap.compose.button.asLineHeight
import com.vk.id.onetap.compose.button.border
import com.vk.id.onetap.compose.button.clip
import com.vk.id.onetap.compose.button.height
import com.vk.id.onetap.compose.button.startAuth

@Composable
public fun AlternateAccountButton(
    modifier: Modifier = Modifier,
    style: AlternateAccountButtonStyle = AlternateAccountButtonStyle.Light(),
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit = {},
    vkid: VKID? = null,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val useVKID = vkid ?: remember {
        VKID(context)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(style.borderStyle, style.cornersStyle)
            .height(style.sizeStyle)
            .clip(style.cornersStyle)
            .clipToBounds()
            .background(style.backgroundStyle)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = style.rippleStyle.asColor(),
                ),
                onClick = {
                    startAuth(
                        coroutineScope,
                        useVKID,
                        onAuth,
                        onFail,
                        VKIDAuthParams { useOAuthProviderIfPossible = false }
                    )
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        BasicText(
            text = stringResource(id = R.string.vkid_auth_use_another_account),
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

@Preview
@Composable
private fun AlternateAccountButtonPreview() {
    AlternateAccountButton(
        onAuth = {},
    )
}
