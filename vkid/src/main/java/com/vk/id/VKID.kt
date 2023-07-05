package com.vk.id

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
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
import com.vk.id.internal.di.VKIDDeps
import com.vk.id.internal.di.VKIDDepsProd
import com.vk.id.internal.store.PrefsStore
import java.lang.ref.WeakReference
import java.security.SecureRandom
import java.util.concurrent.Executors

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
        this.appContext = deps.appContext
        this.api = deps.api
        this.clientId = clientId
        this.clientSecret = clientSecret
        this.redirectUri = redirectUri
        this.authProvidersChooser = deps.authProvidersChooser.value
        this.prefsStore = deps.prefsStore.value
        this.deviceIdProvider = deps.deviceIdProvider.value
        this.pkceGenerator = deps.pkceGenerator.value

        deps.trustedProvidersCache.prefetchSilentAuthProviders()
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
    private val authProvidersChooser: AuthProvidersChooser
    private val prefsStore: PrefsStore
    private val deviceIdProvider: DeviceIdProvider
    private val pkceGenerator: PkceGeneratorSHA256

    private val activeCalls: MutableList<WeakReference<VKIDCall<*>>> = mutableListOf()
    private val executorService = Executors.newCachedThreadPool()

    private var authCallback: AuthCallback? = null

    // todo get session from repo
    public var userSession: UserSession? = null
        private set

    public fun authorize(
        activity: Activity,
        authCallback: AuthCallback,
    ) {
        registerLifeCycleCallback(activity.application, activity)

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

        executorService.execute {
            val fullAuthOptions = createInternalAuthOptions()
            val bestAuthProvider = authProvidersChooser.chooseBest()
            bestAuthProvider.auth(activity, fullAuthOptions)
        }
    }

    private fun createInternalAuthOptions(): AuthOptions {
        val codeVerifier = pkceGenerator.generateRandomCodeVerifier(SecureRandom())
        val codeChallenge = pkceGenerator.deriveCodeVerifierChallenge(codeVerifier)
        prefsStore.codeVerifier = codeVerifier
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        val state = (1..32).map { allowedChars.random() }.joinToString("")
        prefsStore.state = state
        return AuthOptions(
            appId = clientId,
            clientSecret = clientSecret,
            codeChallenge = codeChallenge,
            codeChallengeMethod = "sha256",
            deviceId = deviceIdProvider.getDeviceId(appContext),
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
        val realUuid = deviceIdProvider.getDeviceId(appContext)
        if (realUuid != oauth.uuid) {
            authCallback?.error("invalid_uuid", null)
            return
        }

        val realState = prefsStore.state
        val codeVerifier = prefsStore.codeVerifier

        if (realState != oauth.oauth?.state) {
            authCallback?.error("invalid_state", null)
            return
        }

        val code = oauth.oauth.code
        // execute token request
        executorService.execute {
            val apiCall = api.value.getToken(
                code,
                codeVerifier,
                clientId,
                clientSecret,
                deviceId = realUuid,
                redirectUri
            )
            val callResult = executeCall(apiCall)
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

    private fun <T>executeCall(apiCall: VKIDCall<T>): Result<T>  {
        activeCalls.add(WeakReference(apiCall))
        return apiCall.execute()
    }

    private var lifecycleCallback: Application.ActivityLifecycleCallbacks? = null
    private fun registerLifeCycleCallback(app: Application, activity: Activity) {
        lifecycleCallback = object: Application.ActivityLifecycleCallbacks {
            private var activityRef = WeakReference(activity)
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                // we can loose reference on activity recreation on screen rotate
                if (savedInstanceState?.getBoolean(REGISTERED_ACTIVITY_FLAG) == true) {
                    activityRef = WeakReference(activity)
                }
            }
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                outState.putBoolean(REGISTERED_ACTIVITY_FLAG, true)
            }
            override fun onActivityDestroyed(activity: Activity) {
                if (activityRef.get() === activity && activity.isFinishing) {
                    activeCalls.forEach { it.get()?.cancel() }
                    executorService.shutdown()
                    app.unregisterActivityLifecycleCallbacks(this)
                }
            }
        }
        lifecycleCallback?.let {
            app.registerActivityLifecycleCallbacks(it)
        }
    }
    private companion object {
        const val REGISTERED_ACTIVITY_FLAG = "VKID_CALLBACK_REGISTERED_ACTIVITY_FLAG"
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
