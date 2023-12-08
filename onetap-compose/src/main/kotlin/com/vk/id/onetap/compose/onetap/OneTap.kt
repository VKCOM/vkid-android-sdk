@file:OptIn(InternalVKIDApi::class)

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
import com.vk.id.commn.InternalVKIDApi
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.compose.button.alternate.AlternateAccountButton
import com.vk.id.onetap.compose.button.auth.VKIDButton
import com.vk.id.onetap.compose.button.auth.VKIDButtonSmall
import com.vk.id.onetap.compose.button.auth.VKIDButtonTextProvider
import com.vk.id.onetap.compose.button.auth.rememberVKIDButtonState
import com.vk.id.onetap.compose.button.startAuth

/**
 * Composable function to display a VKID One Tap login interface.
 * For more information how to integrate VK ID Authentication check docs https://id.vk.com/business/go/docs/ru/vkid/latest/vk-id/intro/plan
 *
 * @param modifier Modifier for this composable.
 * @param style The styling for the One Tap interface, default is [OneTapStyle.Light]
 * @param onAuth Callback function invoked on successful authentication with an [AccessToken].
 * @param onFail Callback function invoked on authentication failure with a [VKIDAuthFail] object.
 * @param vkid An optional [VKID] instance to use for authentication.
 *  If instance of VKID is not provided, it will be created on first composition.
 * @param signInAnotherAccountButtonEnabled Flag to enable a button for signing into another account.
 */
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
    val coroutineScope = rememberCoroutineScope()
    if (style is OneTapStyle.Icon) {
        VKIDButtonSmall(style = style.vkidButtonStyle, vkid = vkid, onClick = {
            startAuth(
                coroutineScope,
                useVKID,
                onAuth,
                onFail
            )
        })
    } else {
        OneTap(
            modifier,
            style,
            useVKID,
            signInAnotherAccountButtonEnabled,
            null,
            onVKIDButtonClick = {
                startAuth(
                    coroutineScope,
                    useVKID,
                    onAuth,
                    onFail,
                    VKIDAuthParams {
                        theme = style.toProviderTheme()
                    }
                )
            },
            onAlternateButtonClick = {
                startAuth(
                    coroutineScope,
                    useVKID,
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

@Composable
internal fun OneTap(
    modifier: Modifier = Modifier,
    style: OneTapStyle = OneTapStyle.Light(),
    vkid: VKID,
    signInAnotherAccountButtonEnabled: Boolean = false,
    vkidButtonTextProvider: VKIDButtonTextProvider?,
    onVKIDButtonClick: () -> Unit,
    onAlternateButtonClick: () -> Unit,
) {
    val vkidButtonState = rememberVKIDButtonState()
    Column(modifier = modifier) {
        VKIDButton(
            style = style.vkidButtonStyle,
            state = vkidButtonState,
            vkid = vkid,
            textProvider = vkidButtonTextProvider,
            onClick = onVKIDButtonClick
        )
        if (signInAnotherAccountButtonEnabled) {
            AnimatedVisibility(
                modifier = Modifier.padding(top = 12.dp),
                visible = !vkidButtonState.userLoadFailed,
            ) {
                AlternateAccountButton(
                    style = style.alternateAccountButtonStyle,
                    onClick = onAlternateButtonClick
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
