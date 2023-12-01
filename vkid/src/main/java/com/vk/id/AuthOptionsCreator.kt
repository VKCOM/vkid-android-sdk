package com.vk.id

import android.content.Context
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.auth.pkce.PkceGeneratorSHA256
import com.vk.id.internal.auth.toQueryParam
import com.vk.id.internal.store.PrefsStore
import java.security.SecureRandom

internal class AuthOptionsCreator(
    private val appContext: Context,
    private val pkceGenerator: Lazy<PkceGeneratorSHA256>,
    private val prefsStore: Lazy<PrefsStore>,
    private val serviceCredentials: Lazy<ServiceCredentials>,
    private val deviceIdProvider: Lazy<DeviceIdProvider>
) {
    internal fun create(
        authParams: VKIDAuthParams
    ): AuthOptions {
        val codeVerifier = pkceGenerator.value.generateRandomCodeVerifier(SecureRandom())
        val codeChallenge = pkceGenerator.value.deriveCodeVerifierChallenge(codeVerifier)
        prefsStore.value.codeVerifier = codeVerifier
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')

        @Suppress("MagicNumber")
        val state = (1..32).map { allowedChars.random() }.joinToString("")
        prefsStore.value.state = state
        val locale = authParams.locale ?: VKIDAuthParams.Locale.systemLocale(appContext)
        val theme = authParams.theme ?: VKIDAuthParams.Theme.systemTheme(appContext)
        val credentials = serviceCredentials.value
        return AuthOptions(
            appId = credentials.clientID,
            clientSecret = credentials.clientSecret,
            codeChallenge = codeChallenge,
            codeChallengeMethod = "sha256",
            deviceId = deviceIdProvider.value.getDeviceId(appContext),
            redirectUri = credentials.redirectUri,
            state = state,
            locale = locale?.toQueryParam(),
            theme = theme?.toQueryParam(),
            // To not show "Log in as..." screen in web view
            webAuthPhoneScreen = !authParams.useOAuthProviderIfPossible,
            oAuth = authParams.oAuth,
        )
    }
}
