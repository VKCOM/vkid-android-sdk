package com.vk.id.onetap.compose.onetap

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.compose.button.alternate.AlternateAccountButton
import com.vk.id.onetap.compose.button.auth.VKIDButton
import com.vk.id.onetap.compose.button.auth.VKIDButtonSmall
import com.vk.id.onetap.compose.button.auth.VKIDButtonTextProvider
import com.vk.id.onetap.compose.button.auth.rememberVKIDButtonState
import com.vk.id.onetap.compose.button.startAuth

@Composable
public fun OneTap(
    modifier: Modifier = Modifier,
    style: OneTapStyle = OneTapStyle.Light(),
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit = {},
    vkid: VKID? = null,
    signInAnotherAccountButtonEnabled: Boolean = false
) {
    val context = LocalContext.current
    val useVKID = vkid ?: remember {
        VKID(context)
    }
    if (style is OneTapStyle.Icon) {
        val coroutineScope = rememberCoroutineScope()
        VKIDButtonSmall(style = style.vkidButtonStyle, vkid = vkid, onClick = {
            startAuth(
                coroutineScope,
                useVKID,
                onAuth,
                onFail
            )
        })
    } else {
        OneTap(modifier, style, onAuth, onFail, useVKID, signInAnotherAccountButtonEnabled, null)
    }
}

@Composable
internal fun OneTap(
    modifier: Modifier = Modifier,
    style: OneTapStyle = OneTapStyle.Light(),
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit = {},
    vkid: VKID,
    signInAnotherAccountButtonEnabled: Boolean = false,
    vkidButtonTextProvider: VKIDButtonTextProvider?,
) {
    val coroutineScope = rememberCoroutineScope()
    val vkidButtonState = rememberVKIDButtonState()
    Column(modifier = modifier) {
        VKIDButton(
            style = style.vkidButtonStyle,
            state = vkidButtonState,
            vkid = vkid,
            textProvider = vkidButtonTextProvider,
            onClick = {
                startAuth(
                    coroutineScope,
                    vkid,
                    onAuth,
                    onFail,
                    VKIDAuthParams {
                        theme = style.toProviderTheme()
                    }
                )
            }
        )
        if (signInAnotherAccountButtonEnabled) {
            AnimatedVisibility(
                modifier = Modifier.padding(top = 12.dp),
                visible = !vkidButtonState.userLoadFailed,
            ) {
                AlternateAccountButton(
                    style = style.alternateAccountButtonStyle,
                    onClick = {
                        startAuth(
                            coroutineScope,
                            vkid,
                            onAuth,
                            onFail,
                            VKIDAuthParams {
                                useOAuthProviderIfPossible = false
                                theme = style.toProviderTheme()
                            }
                        )
                    }
                )
            }
        }
    }
}

private fun OneTapStyle.toProviderTheme(): VKIDAuthParams.Theme = when (this) {
    is OneTapStyle.Dark,
    is OneTapStyle.TransparentDark -> VKIDAuthParams.Theme.Dark
    is OneTapStyle.Light,
    is OneTapStyle.TransparentLight,
    is OneTapStyle.Icon -> VKIDAuthParams.Theme.Light
}

@Preview
@Composable
private fun OneTapPreview() {
    OneTap(onAuth = {})
}
