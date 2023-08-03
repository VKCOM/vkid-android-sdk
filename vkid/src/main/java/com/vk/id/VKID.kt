package com.vk.id

import android.app.Activity
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.annotation.WorkerThread
import com.vk.id.internal.api.VKIDApiService
import com.vk.id.internal.auth.AuthEventBridge
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.ExternalAuthResult
import com.vk.id.internal.auth.device.DeviceIdProvider
import com.vk.id.internal.auth.pkce.PkceGeneratorSHA256
import com.vk.id.internal.auth.toExpireTime
import com.vk.id.internal.concurrent.LifecycleAwareExecutor
import com.vk.id.internal.di.VKIDDeps
import com.vk.id.internal.di.VKIDDepsProd
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

    // todo get session from repo
    public var userSession: UserSession? = null
        private set

    public fun authorize(
        activity: Activity,
        authCallback: AuthCallback,
    ) {
        this.authCallback = authCallback

        val alreadyExistingSession = userSession
        if (alreadyExistingSession != null) {
            authCallback.success(alreadyExistingSession)
            return
        }
        val alreadyReceivedAuth = AuthEventBridge.authResult
        if (alreadyReceivedAuth != null) {
            handleExternalAuthResult(alreadyReceivedAuth)
            return
        }

        startActualAuth(activity, authCallback)
    }

    private fun startActualAuth(
        activity: Activity,
        authCallback: AuthCallback,
    ) {
        AuthEventBridge.listener = object : AuthEventBridge.Listener {
            override fun success(oauth: ExternalAuthResult) {
                handleExternalAuthResult(oauth)
            }

            override fun error(message: String, e: Throwable?) {
                authCallback.error(message, e)
            }

            override fun canceled() {
                authCallback.canceled()
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

    private fun handleExternalAuthResult(authResult: ExternalAuthResult) {
        when (authResult) {
            is ExternalAuthResult.Fail -> {
                authCallback?.error(authResult.errorMessage, authResult.error)
                return
            }
            is ExternalAuthResult.Invalid -> {
                authCallback?.canceled()
                return
            }
            is ExternalAuthResult.Success -> {} // ok
        }

        if (authResult.oauth != null) {
            handleOauth(authResult)
        } else {
            val session = UserSession(AccessToken(authResult.token, authResult.userId, authResult.expireTime))
            userSession = session
            authCallback?.success(session)
        }
    }

    private fun handleOauth(oauth: ExternalAuthResult.Success) {
        // validate
        val realUuid = deviceIdProvider.value.getDeviceId(appContext)
        if (realUuid != oauth.uuid) {
            authCallback?.error("invalid_uuid", null)
            return
        }

        val realState = prefsStore.value.state
        val codeVerifier = prefsStore.value.codeVerifier

        if (realState != oauth.oauth?.state) {
            authCallback?.error("invalid_state", null)
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
                authCallback?.error("Failed api request", it)
            }
            callResult.onSuccess { payload ->
                val session =
                    UserSession(AccessToken(payload.accessToken, payload.userId, payload.expiresIn.toExpireTime))
                userSession = session
                authCallback?.success(session)
            }
        }
    }

    public interface AuthCallback {
        @WorkerThread
        public fun success(session: UserSession)
        @WorkerThread
        // todo VKIDError instead of Throwable?
        public fun error(errorMessage: String, error: Throwable?)
        @WorkerThread
        public fun canceled()
    }
}
