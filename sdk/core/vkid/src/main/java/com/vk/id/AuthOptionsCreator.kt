package com.vk.id

import android.content.Context
import android.util.Base64
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.auth.pkce.PkceGeneratorSHA256
import com.vk.id.internal.auth.toQueryParam
import com.vk.id.internal.state.StateGenerator
import com.vk.id.internal.store.PrefsStore
import java.security.SecureRandom

internal class AuthOptionsCreator(
    private val appContext: Context,
    private val pkceGenerator: Lazy<PkceGeneratorSHA256>,
    private val prefsStore: Lazy<PrefsStore>,
    private val serviceCredentials: Lazy<ServiceCredentials>,
    private val deviceIdProvider: Lazy<DeviceIdProvider>,
    private val stateGenerator: StateGenerator,
) {
    internal fun create(
        authParams: VKIDAuthParams
    ): AuthOptions {
        val codeVerifier = pkceGenerator.value.generateRandomCodeVerifier(SecureRandom())
        val codeChallenge = pkceGenerator.value.deriveCodeVerifierChallenge(codeVerifier)
        prefsStore.value.codeVerifier = codeVerifier
        val state = stateGenerator.regenerateState()
        val locale = authParams.locale ?: VKIDAuthParams.Locale.systemLocale(appContext)
        val theme = authParams.theme ?: VKIDAuthParams.Theme.systemTheme(appContext)
        val credentials = serviceCredentials.value
        val deviceId = deviceIdProvider.value.getDeviceId(appContext)
        val redirectUri = "${credentials.redirectUri}?oauth2_params=${getOAuth2Params(deviceId)}"
        return AuthOptions(
            appId = credentials.clientID,
            clientSecret = credentials.clientSecret,
            codeChallenge = codeChallenge,
            codeChallengeMethod = "sha256",
            deviceId = deviceIdProvider.value.getDeviceId(appContext),
            redirectUri = redirectUri,
            state = state,
            locale = locale?.toQueryParam(),
            theme = theme?.toQueryParam(),
            // To not show "Log in as..." screen in web view
            webAuthPhoneScreen = !authParams.useOAuthProviderIfPossible,
            oAuth = authParams.oAuth,
        )
    }

    private fun getOAuth2Params(deviceId: String) = Base64
        .encodeToString("""{"device":"$deviceId"}""".encodeToByteArray(), Base64.DEFAULT)
        .filter { it != '\n' }
}
