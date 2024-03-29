@file:Suppress("MatchingDeclarationName")
@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.onetap.sheet.content

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.Prompt
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.button.startAuth
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle
import kotlinx.coroutines.CoroutineScope
import java.io.Serializable

@Immutable
internal sealed class OneTapBottomSheetAuthStatus : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1
    }

    object Init : OneTapBottomSheetAuthStatus()

    object AuthStarted : OneTapBottomSheetAuthStatus()

    object AuthFailedVKID : OneTapBottomSheetAuthStatus()
    object AuthFailedAlternate : OneTapBottomSheetAuthStatus()
    data class AuthFailedMultibranding(val oAuth: OneTapOAuth) : OneTapBottomSheetAuthStatus()
    object AuthSuccess : OneTapBottomSheetAuthStatus()
}

@Suppress("LongParameterList")
internal fun startVKIDAuth(
    coroutineScope: CoroutineScope,
    vkid: VKID,
    style: OneTapBottomSheetStyle,
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit,
    authStatus: MutableState<OneTapBottomSheetAuthStatus>,
) {
    authStatus.value = OneTapBottomSheetAuthStatus.AuthStarted
    startAuth(
        coroutineScope,
        vkid,
        onAuth = {
            authStatus.value = OneTapBottomSheetAuthStatus.AuthSuccess
            onAuth(it)
        },
        onFail = {
            authStatus.value = OneTapBottomSheetAuthStatus.AuthFailedVKID
            onFail(it)
        },
        VKIDAuthParams {
            theme = style.toProviderTheme()
        }
    )
}

@Suppress("LongParameterList")
internal fun startAlternateAuth(
    coroutineScope: CoroutineScope,
    vkid: VKID,
    style: OneTapBottomSheetStyle,
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit,
    authStatus: MutableState<OneTapBottomSheetAuthStatus>,
) {
    authStatus.value = OneTapBottomSheetAuthStatus.AuthStarted
    startAuth(
        coroutineScope,
        vkid,
        onAuth = {
            authStatus.value = OneTapBottomSheetAuthStatus.AuthSuccess
            onAuth(it)
        },
        onFail = {
            authStatus.value = OneTapBottomSheetAuthStatus.AuthFailedAlternate
            onFail(it)
        },
        VKIDAuthParams {
            useOAuthProviderIfPossible = false
            theme = style.toProviderTheme()
            prompt = Prompt.LOGIN
        }
    )
}

internal fun OneTapBottomSheetStyle.toProviderTheme(): VKIDAuthParams.Theme = when (this) {
    is OneTapBottomSheetStyle.Dark,
    is OneTapBottomSheetStyle.TransparentDark -> VKIDAuthParams.Theme.Dark
    is OneTapBottomSheetStyle.Light,
    is OneTapBottomSheetStyle.TransparentLight -> VKIDAuthParams.Theme.Light
}
