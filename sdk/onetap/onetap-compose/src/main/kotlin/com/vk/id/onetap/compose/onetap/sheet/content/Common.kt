@file:Suppress("MatchingDeclarationName")
@file:OptIn(InternalVKIDApi::class)

package com.vk.id.onetap.compose.onetap.sheet.content

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.vk.id.AccessToken
import com.vk.id.VKIDAuthFail
import com.vk.id.VKIDUser
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.Prompt
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.button.startAuth
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Immutable
internal sealed class OneTapBottomSheetAuthStatus : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1
    }

    object Init : OneTapBottomSheetAuthStatus()

    object AuthStarted : OneTapBottomSheetAuthStatus()

    @Parcelize
    data class AuthFailedVKID(val user: VKIDUser?) : OneTapBottomSheetAuthStatus(), Parcelable
    object AuthFailedAlternate : OneTapBottomSheetAuthStatus()
    data class AuthFailedMultibranding(val oAuth: OneTapOAuth) : OneTapBottomSheetAuthStatus()
    object AuthSuccess : OneTapBottomSheetAuthStatus()
}

@Suppress("LongParameterList")
internal fun startVKIDAuth(
    coroutineScope: CoroutineScope,
    style: OneTapBottomSheetStyle,
    onAuth: (AccessToken) -> Unit,
    onAuthCode: (AuthCodeData, Boolean) -> Unit,
    onFail: (VKIDAuthFail) -> Unit,
    onAuthStatusChange: (OneTapBottomSheetAuthStatus) -> Unit,
    authParams: VKIDAuthUiParams,
    extraAuthParams: Map<String, String>,
    fastAuthEnabled: Boolean,
    user: VKIDUser?,
) {
    onAuthStatusChange(OneTapBottomSheetAuthStatus.AuthStarted)
    startAuth(
        coroutineScope,
        onAuth = {
            onAuthStatusChange(OneTapBottomSheetAuthStatus.AuthSuccess)
            onAuth(it)
        },
        onAuthCode = { data, isCompletion ->
            if (isCompletion) onAuthStatusChange(OneTapBottomSheetAuthStatus.AuthSuccess)
            onAuthCode(data, isCompletion)
        },
        onFail = {
            onAuthStatusChange(OneTapBottomSheetAuthStatus.AuthFailedVKID(user = user))
            onFail(it)
        },
        authParams.asParamsBuilder {
            theme = style.toProviderTheme()
            extraParams = extraAuthParams
            if (!fastAuthEnabled) {
                useOAuthProviderIfPossible = false
                prompt = Prompt.LOGIN
            } else if (user == null) {
                prompt = Prompt.CONSENT
            }
        }
    )
}

@Suppress("LongParameterList")
internal fun startAlternateAuth(
    coroutineScope: CoroutineScope,
    style: OneTapBottomSheetStyle,
    onAuth: (AccessToken) -> Unit,
    onAuthCode: (AuthCodeData, Boolean) -> Unit,
    onFail: (VKIDAuthFail) -> Unit,
    onAuthStatusChange: (OneTapBottomSheetAuthStatus) -> Unit,
    authParams: VKIDAuthUiParams,
    extraAuthParams: Map<String, String>
) {
    onAuthStatusChange(OneTapBottomSheetAuthStatus.AuthStarted)
    startAuth(
        coroutineScope,
        onAuth = {
            onAuthStatusChange(OneTapBottomSheetAuthStatus.AuthSuccess)
            onAuth(it)
        },
        onAuthCode = onAuthCode,
        onFail = {
            onAuthStatusChange(OneTapBottomSheetAuthStatus.AuthFailedAlternate)
            onFail(it)
        },
        authParams.asParamsBuilder {
            useOAuthProviderIfPossible = false
            theme = style.toProviderTheme()
            prompt = Prompt.LOGIN
            extraParams = extraAuthParams
        }
    )
}

internal fun OneTapBottomSheetStyle.toProviderTheme(): VKIDAuthParams.Theme = when (this) {
    is OneTapBottomSheetStyle.Dark,
    is OneTapBottomSheetStyle.TransparentDark -> VKIDAuthParams.Theme.Dark

    is OneTapBottomSheetStyle.Light,
    is OneTapBottomSheetStyle.TransparentLight -> VKIDAuthParams.Theme.Light
}
