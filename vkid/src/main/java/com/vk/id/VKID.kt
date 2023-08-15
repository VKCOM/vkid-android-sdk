package com.vk.id

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.SystemClock
import androidx.annotation.VisibleForTesting
import androidx.annotation.WorkerThread
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.AuthEventBridge
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.AuthResult
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.auth.pkce.PkceGeneratorSHA256
import com.vk.id.internal.auth.toExpireTime
import com.vk.id.internal.concurrent.LifecycleAwareExecutor
import com.vk.id.internal.di.VKIDDeps
import com.vk.id.internal.di.VKIDDepsProd
import com.vk.id.internal.log.AndroidLogcatLogEngine
import com.vk.id.internal.log.FakeLogEngine
import com.vk.id.internal.log.LogEngine
import com.vk.id.internal.log.VKIDLog
import com.vk.id.internal.log.createLoggerForClass
import com.vk.id.internal.store.PrefsStore
import java.security.SecureRandom

public inline fun VKID (initializer: VKID.Builder.() -> Unit): VKID {
    return VKID.Builder().apply(initializer).build()
}

public class VKID {
    private constructor(context: Context, clientId: String, clientSecret: String, redirectUri: String) : this(
        clientId,
        clientSecret,
        redirectUri,
        VKIDDepsProd(context, clientId, clientSecret)
    )

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
    internal constructor(clientId: String, clientSecret: String, redirectUri: String, deps: VKIDDeps) {
        this.api = deps.api
        this.appContext = deps.appContext
        this.clientId = clientId
        this.clientSecret = clientSecret
        this.redirectUri = redirectUri
        this.authProvidersChooser = deps.authProvidersChooser
        this.prefsStore = deps.prefsStore
        this.deviceIdProvider = deps.deviceIdProvider
        this.pkceGenerator = deps.pkceGenerator
        this.executor = deps.lifeCycleAwareExecutor
    }

    public class Builder {
        public var context: Context? = null
        public var clientId: String? = null
        public var clientSecret: String? = null
        public var redirectUri: String? = null

        // todo check context not set and throw error
        public fun build(): VKID = VKID(context!!.applicationContext, clientId!!, clientSecret!!, redirectUri!!)
    }

    private val logger = createLoggerForClass()

    private val clientId: String
    private val clientSecret: String
    private val redirectUri: String

    private val api: Lazy<VKIDApiService>
    private val appContext: Context
    private val authProvidersChooser: Lazy<AuthProvidersChooser>
    private val prefsStore: Lazy<PrefsStore>
    private val deviceIdProvider: Lazy<DeviceIdProvider>
    private val pkceGenerator: Lazy<PkceGeneratorSHA256>
    private val executor: Lazy<LifecycleAwareExecutor>

    private var authCallback: AuthCallback? = null

    public fun authorize(
        activity: Activity,
        authCallback: AuthCallback,
    ) {
        this.authCallback = authCallback

        AuthEventBridge.listener = object : AuthEventBridge.Listener {
            override fun onAuthResult(authResult: AuthResult) {
                handleAuthResult(authResult)
            }
        }

        executor.value.attachActivity(activity)
        executor.value.execute {
            val fullAuthOptions = createInternalAuthOptions()
            val bestAuthProvider = authProvidersChooser.value.chooseBest()
            bestAuthProvider.auth(activity, fullAuthOptions)
        }
    }

    private fun createInternalAuthOptions(): AuthOptions {
        val codeVerifier = pkceGenerator.value.generateRandomCodeVerifier(SecureRandom())
        val codeChallenge = pkceGenerator.value.deriveCodeVerifierChallenge(codeVerifier)
        prefsStore.value.codeVerifier = codeVerifier
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        val state = (1..32).map { allowedChars.random() }.joinToString("")
        prefsStore.value.state = state
        return AuthOptions(
            appId = clientId,
            clientSecret = clientSecret,
            codeChallenge = codeChallenge,
            codeChallengeMethod = "sha256",
            deviceId = deviceIdProvider.value.getDeviceId(appContext),
            redirectUri = redirectUri,
            state = state,
        )
    }

    private fun handleAuthResult(authResult: AuthResult) {
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

    private fun handleOauth(oauth: AuthResult.Success) {
        // validate
        val realUuid = deviceIdProvider.value.getDeviceId(appContext)
        if (realUuid != oauth.uuid) {
            logger.error("Invalid oauth UUID, want $realUuid but received ${oauth.uuid}", null)
            authCallback?.onFail(VKIDAuthFail.FailedOAuthState("Invalid uuid"))
            return
        }

        val realState = prefsStore.value.state
        val codeVerifier = prefsStore.value.codeVerifier

        if (realState != oauth.oauth?.state) {
            logger.error("Invalid oauth state, want $realState but received ${oauth.oauth?.state}", null)
            authCallback?.onFail(VKIDAuthFail.FailedOAuthState("Invalid state"))
            return
        }

        val code = oauth.oauth.code
        // execute token request
        executor.value.execute {
            val apiCall = api.value.getToken(
                code,
                codeVerifier,
                clientId,
                clientSecret,
                deviceId = realUuid,
                redirectUri
            )
            val callResult = executor.value.executeCall(apiCall)
            callResult.onFailure {
                authCallback?.onFail(VKIDAuthFail.FailedApiCall("Failed code to token exchange api call", it))
            }
            callResult.onSuccess { payload ->
                authCallback?.onSuccess(AccessToken(payload.accessToken, payload.userId, payload.expiresIn.toExpireTime))
            }
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
        @WorkerThread
        public fun onSuccess(accessToken: AccessToken)
        @WorkerThread
        public fun onFail(fail: VKIDAuthFail)
    }
}
