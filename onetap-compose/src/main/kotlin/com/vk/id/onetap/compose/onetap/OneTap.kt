package com.vk.id.onetap.compose.onetap

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.compose.button.VKIDButton
import com.vk.id.onetap.compose.button.VKIDButtonStyle
import com.vk.id.onetap.compose.button.VKIDButtonTextProvider
import com.vk.id.onetap.compose.button.alternate.AlternateAccountButton
import com.vk.id.onetap.compose.button.alternate.style.AlternateAccountButtonStyle
import com.vk.id.onetap.compose.button.rememberVKIDButtonState

@Composable
public fun OneTap(
    modifier: Modifier = Modifier,
    // todo OneTapStyle
    style: VKIDButtonStyle = VKIDButtonStyle.Blue(),
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit = {},
    vkid: VKID? = null,
    signInAnotherAccountButtonEnabled: Boolean = false
) {
    OneTap(modifier, style, onAuth, onFail, vkid, signInAnotherAccountButtonEnabled, null)
}

@Composable
internal fun OneTap(
    modifier: Modifier = Modifier,
    // todo OneTapStyle
    style: VKIDButtonStyle = VKIDButtonStyle.Blue(),
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit = {},
    vkid: VKID? = null,
    signInAnotherAccountButtonEnabled: Boolean = false,
    vkidButtonTextProvider: VKIDButtonTextProvider?
) {
    val context = LocalContext.current
    val useVKID = vkid ?: remember {
        VKID(context)
    }
    val vkidButtonState = rememberVKIDButtonState()
    Column(modifier = modifier) {
        VKIDButton(
            style = style,
            onAuth = onAuth,
            onFail = onFail,
            state = vkidButtonState,
            vkid = useVKID,
            textProvider = vkidButtonTextProvider
        )
        if (signInAnotherAccountButtonEnabled) {
            AnimatedVisibility(
                modifier = Modifier.padding(top = 12.dp),
                visible = !vkidButtonState.userLoadFailed,
            ) {
                AlternateAccountButton(
                    style = style.toAlternate(),
                    onAuth = onAuth,
                    onFail = onFail,
                    vkid = useVKID
                )
            }
        }
    }
}

private fun VKIDButtonStyle.toAlternate(): AlternateAccountButtonStyle = when (this) {
    is VKIDButtonStyle.TransparentLight -> AlternateAccountButtonStyle.Dark(
        borderStyle = this.borderStyle,
        cornersStyle = this.cornersStyle,
        sizeStyle = this.sizeStyle,
    )
    is VKIDButtonStyle.Blue,
    is VKIDButtonStyle.TransparentDark -> AlternateAccountButtonStyle.Light(
        borderStyle = this.borderStyle,
        cornersStyle = this.cornersStyle,
        sizeStyle = this.sizeStyle,
    )
}

@Preview
@Composable
private fun OneTapPreview() {
    OneTap(onAuth = {})
}
