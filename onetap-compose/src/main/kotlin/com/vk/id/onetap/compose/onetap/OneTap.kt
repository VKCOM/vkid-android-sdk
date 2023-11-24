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
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.compose.button.alternate.AlternateAccountButton
import com.vk.id.onetap.compose.button.auth.VKIDButton
import com.vk.id.onetap.compose.button.auth.VKIDButtonSmall
import com.vk.id.onetap.compose.button.auth.VKIDButtonTextProvider
import com.vk.id.onetap.compose.button.auth.rememberVKIDButtonState

@Composable
public fun OneTap(
    modifier: Modifier = Modifier,
    style: OneTapStyle = OneTapStyle.Light(),
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit = {},
    vkid: VKID? = null,
    signInAnotherAccountButtonEnabled: Boolean = false
) {
    if (style is OneTapStyle.Icon) {
        VKIDButtonSmall(style = style.vkidButtonStyle, onAuth = onAuth, onFail = onFail, vkid = vkid)
    } else {
        OneTap(modifier, style, onAuth, onFail, vkid, signInAnotherAccountButtonEnabled, null)
    }
}

@Composable
internal fun OneTap(
    modifier: Modifier = Modifier,
    style: OneTapStyle = OneTapStyle.Light(),
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
            style = style.vkidButtonStyle,
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
                    style = style.alternateAccountButtonStyle,
                    onAuth = onAuth,
                    onFail = onFail,
                    vkid = useVKID
                )
            }
        }
    }
}

@Preview
@Composable
private fun OneTapPreview() {
    OneTap(onAuth = {})
}
