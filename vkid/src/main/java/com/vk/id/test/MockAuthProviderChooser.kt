@file:OptIn(InternalVKIDApi::class, InternalVKIDApi::class)

package com.vk.id.test

import android.content.Context
import android.content.Intent
import com.vk.id.VKIDUser
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.commn.InternalVKIDApi
import com.vk.id.internal.auth.AuthActivity
import com.vk.id.internal.auth.AuthEventBridge
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.AuthResult
import com.vk.id.internal.auth.VKIDAuthProvider

internal class MockAuthProviderChooser(
    private val context: Context,
    private val config: MockAuthProviderConfig
) : AuthProvidersChooser {

    override suspend fun chooseBest(params: VKIDAuthParams) = VKIDAuthProvider {
        startTestActivity(it)
    }

    private fun startTestActivity(it: AuthOptions) {
        if (config.notifyNoBrowserAvailable) {
            AuthEventBridge.onAuthResult(
                AuthResult.NoBrowserAvailable("", IllegalStateException("Mocked no available browser"))
            )
        }
        if (config.notifyFailedRedirectActivity){
            AuthEventBridge.onAuthResult(
                AuthResult.AuthActiviyResultFailed("", IllegalStateException("Mocked activity result failed"))
            )
        }
        val intent = Intent(context, TestAuthProviderActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("appId", it.appId)
        intent.putExtra("clientSecret", it.clientSecret)
        intent.putExtra("codeChallenge", it.codeChallenge)
        intent.putExtra("codeChallengeMethod", it.codeChallengeMethod)
        intent.putExtra("deviceId", config.overrideUuid ?: it.deviceId)
        intent.putExtra("redirectUri", it.redirectUri)
        intent.putExtra("state", config.overrideState ?: it.state)
        intent.putExtra("locale", it.locale)
        intent.putExtra("theme", it.theme)
        intent.putExtra("webAuthPhoneScreen", it.webAuthPhoneScreen)
        intent.putExtra("oAuth", it.oAuth)
        intent.putExtra("overrideOAuthToNull", config.overrideOAuthToNull)
        intent.putExtra("user", config.user)
        AuthActivity.startForAuth(context, intent)
    }
}

internal data class MockAuthProviderConfig(
    val overrideUuid: String? = null,
    val overrideState: String? = null,
    val overrideOAuthToNull: Boolean = false,
    val user: VKIDUser? = null,
    val notifyNoBrowserAvailable: Boolean = false,
    val notifyFailedRedirectActivity: Boolean = false,
)