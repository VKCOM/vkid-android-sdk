@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.onetap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.commn.InternalVKIDApi
import com.vk.id.multibranding.OAuthListWidget
import com.vk.id.multibranding.common.callback.OAuthListWidgetAuthCallback
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.common.OneTapStyle
import com.vk.id.onetap.compose.button.alternate.AdaptiveAlternateAccountButton
import com.vk.id.onetap.compose.button.auth.VKIDButton
import com.vk.id.onetap.compose.button.auth.VKIDButtonSmall
import com.vk.id.onetap.compose.button.auth.VKIDButtonTextProvider
import com.vk.id.onetap.compose.button.auth.rememberVKIDButtonState
import com.vk.id.onetap.compose.button.startAuth
import com.vk.id.onetap.compose.util.PlaceComposableIfFitsWidth

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
 *  Note that if text doesn't fit the available width the view will be hidden regardless of the flag.
 */
@Composable
public fun OneTap(
    modifier: Modifier = Modifier,
    style: OneTapStyle = OneTapStyle.Light(),
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit = { },
    vkid: VKID? = null,
    signInAnotherAccountButtonEnabled: Boolean = false
): Unit = OneTap(
    modifier = modifier,
    style = style,
    onAuth = { _, accessToken -> onAuth(accessToken) },
    onFail = { _, fail -> onFail(fail) },
    oAuths = emptySet(),
    vkid = vkid,
    signInAnotherAccountButtonEnabled = signInAnotherAccountButtonEnabled
)

/**
 * Composable function to display a VKID One Tap login interface with multibranding.
 * For more information how to integrate VK ID Authentication check docs https://id.vk.com/business/go/docs/ru/vkid/latest/vk-id/intro/plan
 *
 * @param modifier Modifier for this composable.
 * @param style The styling for the One Tap interface, default is [OneTapStyle.Light]
 * @param onAuth Callback function invoked on successful authentication with an [OneTapOAuth] and an [AccessToken].
 * The first parameter is the OAuth which was used for authorization or null if the main flow with OneTap was used.
 * The second parameter is the access token to be used for working with VK API.
 * @param onFail Callback function invoked on authentication failure with on [OneTapOAuth] and a [VKIDAuthFail] object.
 * The first parameter is the OAuth which was used for authorization or null if the main flow with OneTap was used.
 * The second parameter is the error which happened during authorization.
 * @param oAuths A set of OAuths to be displayed.
 * @param vkid An optional [VKID] instance to use for authentication.
 *  If instance of VKID is not provided, it will be created on first composition.
 * @param signInAnotherAccountButtonEnabled Flag to enable a button for signing into another account.
 *  Note that if text doesn't fit the available width the view will be hidden regardless of the flag.
 */
@Composable
@Suppress("LongMethod")
public fun OneTap(
    modifier: Modifier = Modifier,
    style: OneTapStyle = OneTapStyle.Light(),
    onAuth: (OneTapOAuth?, AccessToken) -> Unit,
    onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit = { _, _ -> },
    oAuths: Set<OneTapOAuth> = emptySet(),
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
                { onAuth(null, it) },
                { onFail(null, it) }
            )
        })
    } else {
        PlaceComposableIfFitsWidth(
            modifier = modifier,
            measureModifier = Modifier.fillMaxWidth(),
            viewToMeasure = {
                OneTap(
                    modifier = it,
                    style = style,
                    oAuths = oAuths,
                    vkid = useVKID,
                    signInAnotherAccountButtonEnabled = signInAnotherAccountButtonEnabled,
                    vkidButtonTextProvider = null,
                    onVKIDButtonClick = {
                        startAuth(
                            coroutineScope,
                            useVKID,
                            { onAuth(null, it) },
                            { onFail(null, it) },
                            VKIDAuthParams {
                                theme = style.toProviderTheme()
                            }
                        )
                    },
                    onAlternateButtonClick = {
                        startAuth(
                            coroutineScope,
                            useVKID,
                            { onAuth(null, it) },
                            { onFail(null, it) },
                            VKIDAuthParams {
                                useOAuthProviderIfPossible = false
                                theme = style.toProviderTheme()
                            }
                        )
                    },
                    onAuth = onAuth,
                    onFail = onFail,
                )
            },
            fallback = {
                VKIDButtonSmall(style = style.vkidButtonStyle, vkid = vkid, onClick = {
                    startAuth(
                        coroutineScope,
                        useVKID,
                        { onAuth(null, it) },
                        { onFail(null, it) }
                    )
                })
            }
        )
    }
}

@Suppress("LongParameterList")
@Composable
internal fun OneTap(
    modifier: Modifier = Modifier,
    style: OneTapStyle = OneTapStyle.Light(),
    oAuths: Set<OneTapOAuth>,
    vkid: VKID,
    signInAnotherAccountButtonEnabled: Boolean = false,
    vkidButtonTextProvider: VKIDButtonTextProvider?,
    onVKIDButtonClick: () -> Unit,
    onAlternateButtonClick: () -> Unit,
    onAuth: (OneTapOAuth?, AccessToken) -> Unit,
    onFail: (OneTapOAuth?, VKIDAuthFail) -> Unit,
) {
    val vkidButtonState = rememberVKIDButtonState()
    Column(modifier = modifier) {
        VKIDButton(
            modifier = Modifier.testTag("vkid_button"),
            style = style.vkidButtonStyle,
            state = vkidButtonState,
            vkid = vkid,
            textProvider = vkidButtonTextProvider,
            onClick = onVKIDButtonClick
        )
        if (signInAnotherAccountButtonEnabled) {
            AdaptiveAlternateAccountButton(
                vkidButtonState = vkidButtonState,
                style = style.alternateAccountButtonStyle,
                onAlternateButtonClick,
            )
        }
        if (oAuths.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            OAuthListWidget(
                vkid = vkid,
                onAuth = OAuthListWidgetAuthCallback.WithOAuth { oAuth, accessToken ->
                    onAuth(OneTapOAuth.fromOAuth(oAuth), accessToken)
                },
                onFail = { oAuth, fail -> onFail(OneTapOAuth.fromOAuth(oAuth), fail) },
                style = style.oAuthListWidgetStyle,
                oAuths = oAuths.map { it.toOAuth() }.toSet(),
            )
        }
    }
}

private fun OneTapStyle.toProviderTheme(): VKIDAuthParams.Theme? = when (this) {
    is OneTapStyle.Dark,
    is OneTapStyle.TransparentDark -> VKIDAuthParams.Theme.Dark

    is OneTapStyle.Light,
    is OneTapStyle.TransparentLight -> VKIDAuthParams.Theme.Light

    is OneTapStyle.Icon -> null
}

@Preview
@Composable
private fun OneTapPreview() {
    OneTap(onAuth = { _, _ -> })
}
