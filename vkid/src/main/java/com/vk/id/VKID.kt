package com.vk.id

import android.content.Context
import android.os.Build
import android.os.SystemClock
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.AuthEventBridge
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.AuthResult
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.auth.pkce.PkceGeneratorSHA256
import com.vk.id.internal.auth.toExpireTime
import com.vk.id.internal.auth.toQueryParam
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.di.VKIDDeps
import com.vk.id.internal.di.VKIDDepsProd
import com.vk.id.internal.log.AndroidLogcatLogEngine
import com.vk.id.internal.log.FakeLogEngine
import com.vk.id.internal.log.LogEngine
import com.vk.id.internal.log.VKIDLog
import com.vk.id.internal.log.createLoggerForClass
import com.vk.id.internal.store.PrefsStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.SecureRandom

public class VKID {
    public constructor(context: Context) : this(VKIDDepsProd(context))

    public companion object {
        @Suppress("MemberVisibilityCanBePrivate")
        public var logEngine: LogEngine = AndroidLogcatLogEngine()
            set(value) {
                field = value
                VKIDLog.setLogEngine(value)
            }
        public var logsEnabled: Boolean = false
            set(value) {
                field = value
                if (value) {
                    VKIDLog.setLogEngine(logEngine)
                } else {
                    VKIDLog.setLogEngine(FakeLogEngine())
                }
            }
    }

    /**
     * Only for tests, to provide mocked dependencies
     */
    @VisibleForTesting
    internal constructor(deps: VKIDDeps) {
        this.api = deps.api
        this.appContext = deps.appContext
        this.authProvidersChooser = deps.authProvidersChooser
        this.deviceIdProvider = deps.deviceIdProvider
        this.dispatchers = deps.dispatchers
        this.prefsStore = deps.prefsStore
        this.pkceGenerator = deps.pkceGenerator
        this.serviceCredentials = deps.serviceCredentials
    }

    private val logger = createLoggerForClass()

    private val api: Lazy<VKIDApiService>
    private val appContext: Context
    private val authProvidersChooser: Lazy<AuthProvidersChooser>
    private val deviceIdProvider: Lazy<DeviceIdProvider>
    private val pkceGenerator: Lazy<PkceGeneratorSHA256>
    private val dispatchers: CoroutinesDispatchers
    private val prefsStore: Lazy<PrefsStore>
    private val serviceCredentials: Lazy<ServiceCredentials>

    private var authCallback: AuthCallback? = null

    public fun authorize(
        lifecycleOwner: LifecycleOwner,
        authCallback: AuthCallback,
        authParams: VKIDAuthParams = VKIDAuthParams {},
    ) {
        lifecycleOwner.lifecycleScope.launch {
            authorize(authCallback, authParams)
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    public suspend fun authorize(authCallback: AuthCallback, authParams: VKIDAuthParams = VKIDAuthParams {}) {
        this.authCallback = authCallback
        val authContext = currentCoroutineContext()

        AuthEventBridge.listener = object : AuthEventBridge.Listener {
            override fun onAuthResult(authResult: AuthResult) {
                CoroutineScope(authContext + Job()).launch {
                    handleAuthResult(authResult)
                }
            }
        }

        withContext(dispatchers.IO) {
            val fullAuthOptions = createInternalAuthOptions(authParams)
            val bestAuthProvider = authProvidersChooser.value.chooseBest()
            bestAuthProvider.auth(appContext, fullAuthOptions)
        }
    }

    private fun createInternalAuthOptions(authParams: VKIDAuthParams): AuthOptions {
        val codeVerifier = pkceGenerator.value.generateRandomCodeVerifier(SecureRandom())
        val codeChallenge = pkceGenerator.value.deriveCodeVerifierChallenge(codeVerifier)
        prefsStore.value.codeVerifier = codeVerifier
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
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
        )
    }

    private suspend fun handleAuthResult(authResult: AuthResult) {
        when (authResult) {
            is AuthResult.Canceled -> {
                authCallback?.onFail(VKIDAuthFail.Canceled(authResult.message))
                return
            }

            is AuthResult.NoBrowserAvailable -> {
                authCallback?.onFail(VKIDAuthFail.NoBrowserAvailable(authResult.message, authResult.error))
                return
            }

            is AuthResult.AuthActiviyResultFailed -> {
                authCallback?.onFail(VKIDAuthFail.FailedRedirectActivity(authResult.message, authResult.error))
                return
            }

            is AuthResult.Success -> {
                // We do not stop auth here in hope that it still be success,
                // but if not there will be error response from backend
                if (authResult.expireTime < currentTime()) {
                    logger.error("OAuth code is old, there is a big chance auth will fail", null)
                }
            }
        }

        if (authResult.oauth != null) {
            handleOauth(authResult)
        } else {
            authCallback?.onSuccess(AccessToken(authResult.token, authResult.userId, authResult.expireTime))
        }
    }

    private suspend fun handleOauth(oauth: AuthResult.Success) {
        lateinit var realUuid: String
        lateinit var realState: String
        lateinit var codeVerifier: String
        withContext(dispatchers.IO) {
            realUuid = deviceIdProvider.value.getDeviceId(appContext)
            realState = prefsStore.value.state
            codeVerifier = prefsStore.value.codeVerifier
        }

        if (realUuid != oauth.uuid) {
            logger.error("Invalid oauth UUID, want $realUuid but received ${oauth.uuid}", null)
            authCallback?.onFail(VKIDAuthFail.FailedOAuthState("Invalid uuid"))
            return
        }

        if (realState != oauth.oauth?.state) {
            logger.error("Invalid oauth state, want $realState but received ${oauth.oauth?.state}", null)
            authCallback?.onFail(VKIDAuthFail.FailedOAuthState("Invalid state"))
            return
        }

        val creds = serviceCredentials.value
        val code = oauth.oauth.code
        // execute token request
        val callResult = withContext(dispatchers.IO) {
            api.value.getToken(
                code,
                codeVerifier,
                creds.clientID,
                creds.clientSecret,
                deviceId = realUuid,
                creds.redirectUri,
            ).execute()
        }
        callResult.onFailure {
            authCallback?.onFail(VKIDAuthFail.FailedApiCall("Failed code to token exchange api call", it))
        }
        callResult.onSuccess { payload ->
            authCallback?.onSuccess(AccessToken(payload.accessToken, payload.userId, payload.expiresIn.toExpireTime))
        }
    }

    private fun currentTime(): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            SystemClock.currentNetworkTimeClock().millis()
        } else {
            System.currentTimeMillis()
        }
    }

    public interface AuthCallback {
        public fun onSuccess(accessToken: AccessToken)
        public fun onFail(fail: VKIDAuthFail)
    }
}
