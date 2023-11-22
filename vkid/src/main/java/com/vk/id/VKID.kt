package com.vk.id

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.AuthEventBridge
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.AuthResult
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.auth.toExpireTime
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.di.VKIDDeps
import com.vk.id.internal.di.VKIDDepsProd
import com.vk.id.internal.ipc.VkSilentAuthInfoProvider
import com.vk.id.internal.log.AndroidLogcatLogEngine
import com.vk.id.internal.log.FakeLogEngine
import com.vk.id.internal.log.LogEngine
import com.vk.id.internal.log.VKIDLog
import com.vk.id.internal.log.createLoggerForClass
import com.vk.id.internal.store.PrefsStore
import com.vk.id.internal.user.UserDataFetcher
import com.vk.id.internal.util.currentTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        this.authOptionsCreator = deps.authOptionsCreator
        this.deviceIdProvider = deps.deviceIdProvider
        this.dispatchers = deps.dispatchers
        this.prefsStore = deps.prefsStore
        this.serviceCredentials = deps.serviceCredentials
        this.vkSilentAuthInfoProvider = deps.vkSilentAuthInfoProvider
        this.userDataFetcher = deps.userDataFetcher

        logger.info(
            "VKID initialized\nVersion name: ${BuildConfig.VKID_VERSION_NAME}\nCI build: ${BuildConfig.CI_BUILD_NUMBER} ${BuildConfig.CI_BUILD_TYPE}"
        )
    }

    private val logger = createLoggerForClass()

    private val api: Lazy<VKIDApiService>
    private val appContext: Context
    private val authProvidersChooser: Lazy<AuthProvidersChooser>
    private val authOptionsCreator: AuthOptionsCreator
    private val deviceIdProvider: Lazy<DeviceIdProvider>
    private val dispatchers: CoroutinesDispatchers
    private val prefsStore: Lazy<PrefsStore>
    private val serviceCredentials: Lazy<ServiceCredentials>
    private val vkSilentAuthInfoProvider: Lazy<VkSilentAuthInfoProvider>
    private val userDataFetcher: Lazy<UserDataFetcher>

    private var authCallbacks = mutableSetOf<AuthCallback>()

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
    public suspend fun authorize(
        authCallback: AuthCallback,
        authParams: VKIDAuthParams = VKIDAuthParams {}
    ) {
        this.authCallbacks.add(authCallback)
        val authContext = currentCoroutineContext()

        AuthEventBridge.listener = object : AuthEventBridge.Listener {
            override fun onAuthResult(authResult: AuthResult) {
                CoroutineScope(authContext + Job()).launch {
                    handleAuthResult(authResult)
                }
            }
        }

        withContext(dispatchers.io) {
            val bestAuthProvider = authProvidersChooser.value.chooseBest(authParams)
            val fullAuthOptions = authOptionsCreator.create(authParams)
            bestAuthProvider.auth(appContext, fullAuthOptions)
        }
    }

    public suspend fun fetchUserData(): Result<VKIDUser?> {
        return Result.success(userDataFetcher.value.fetchUserData())
    }

    private suspend fun handleAuthResult(authResult: AuthResult) {
        if (authResult !is AuthResult.Success) {
            emitAuthFail(authResult.toVKIDAuthFail())
            return
        }
        // We do not stop auth here in hope that it still be success,
        // but if not there will be error response from backend
        if (authResult.expireTime < currentTime()) {
            logger.error("OAuth code is old, there is a big chance auth will fail", null)
        }

        if (authResult.oauth != null) {
            handleOauth(authResult)
        } else {
            emitAuthSuccess(
                AccessToken(
                    authResult.token,
                    authResult.userId,
                    authResult.expireTime
                )
            )
        }
    }

    private suspend fun handleOauth(oauth: AuthResult.Success) {
        lateinit var realUuid: String
        lateinit var realState: String
        lateinit var codeVerifier: String
        withContext(dispatchers.io) {
            realUuid = deviceIdProvider.value.getDeviceId(appContext)
            realState = prefsStore.value.state
            codeVerifier = prefsStore.value.codeVerifier
        }

        if (realUuid != oauth.uuid) {
            logger.error("Invalid oauth UUID, want $realUuid but received ${oauth.uuid}", null)
            emitAuthFail(VKIDAuthFail.FailedOAuthState("Invalid uuid"))
            return
        }

        if (realState != oauth.oauth?.state) {
            logger.error(
                "Invalid oauth state, want $realState but received ${oauth.oauth?.state}",
                null
            )
            emitAuthFail(VKIDAuthFail.FailedOAuthState("Invalid state"))
            return
        }

        val creds = serviceCredentials.value
        val code = oauth.oauth.code
        // execute token request
        val callResult = withContext(dispatchers.io) {
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
            emitAuthFail(
                VKIDAuthFail.FailedApiCall(
                    "Failed code to token exchange api call",
                    it
                )
            )
        }
        callResult.onSuccess { payload ->
            emitAuthSuccess(
                AccessToken(
                    payload.accessToken,
                    payload.userId,
                    payload.expiresIn.toExpireTime
                )
            )
        }
    }

    private fun AuthResult.toVKIDAuthFail() = when (this) {
        is AuthResult.Canceled -> VKIDAuthFail.Canceled(message)
        is AuthResult.NoBrowserAvailable -> VKIDAuthFail.NoBrowserAvailable(
            message,
            error
        )
        is AuthResult.AuthActiviyResultFailed -> VKIDAuthFail.FailedRedirectActivity(
            message,
            error
        )
        is AuthResult.Success -> error("AuthResult is Success and cannot be converted to fail!")
    }

    private fun emitAuthSuccess(token: AccessToken) {
        authCallbacks.forEach {
            it.onSuccess(token)
        }
    }

    private fun emitAuthFail(fail: VKIDAuthFail) {
        authCallbacks.forEach {
            it.onFail(fail)
        }
    }

    public interface AuthCallback {
        public fun onSuccess(accessToken: AccessToken)
        public fun onFail(fail: VKIDAuthFail)
    }
}
