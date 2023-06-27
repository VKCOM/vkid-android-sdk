package com.vk.id

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.annotation.WorkerThread
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.ExternalOauthResult
import com.vk.id.internal.auth.OAuthEventBridge
import com.vk.id.internal.di.VKIDDeps
import com.vk.id.internal.di.VKIDDepsProd
import java.lang.ref.WeakReference
import java.util.concurrent.Executors

public inline fun VKID (initializer: VKID.Builder.() -> Unit): VKID {
    return VKID.Builder().apply(initializer).build()
}

public class VKID {
    private constructor(context: Context, clientId: String, clientSecret: String, redirectUri: String) : this(
        clientId,
        clientSecret,
        redirectUri,
        VKIDDepsProd(context)
    )

    /**
     * Only for tests, to provide mocked dependencies
     */
    @VisibleForTesting
    internal constructor(clientId: String, clientSecret: String, redirectUri: String, deps: VKIDDeps) {
        this.appContext = deps.appContext
        this.clientId = clientId
        this.clientSecret = clientSecret
        this.redirectUri = redirectUri
        this.authProvidersChooser = deps.authProvidersChooser.value
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

    private val appContext: Context
    private val authProvidersChooser: AuthProvidersChooser

    private val activeCalls: MutableList<WeakReference<VKIDCall<*>>> = mutableListOf()
    private val executorService = Executors.newSingleThreadExecutor()

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
        val alreadyReceivedOauth = OAuthEventBridge.oauthResult
        if (alreadyReceivedOauth != null) {
            handleExternalOauthResult(alreadyReceivedOauth)
            return
        }

        startActualAuth(activity, authCallback)
    }

    private fun startActualAuth(
        activity: Activity,
        authCallback: AuthCallback,
    ) {
        OAuthEventBridge.listener = object : OAuthEventBridge.Listener {
            override fun success(oauth: ExternalOauthResult) {
                handleExternalOauthResult(oauth)
            }

            override fun error(e: Throwable) {
                authCallback.error(e)
            }

            override fun canceled() {
                authCallback.canceled()
            }
        }

        val fullAuthOptions = createInternalAuthOptions()
        val bestAuthProvider = authProvidersChooser.chooseBest()
        bestAuthProvider.auth(activity, fullAuthOptions)
    }

    private fun createInternalAuthOptions(): AuthOptions {
        TODO()
    }

    private fun handleExternalOauthResult(oauth: ExternalOauthResult) {
        if (oauth !is ExternalOauthResult.Success) {
            return
        }
        // validate
        TODO()
        // execute token request
        /*
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
                authCallback?.error(it)
            }
            callResult.onSuccess { payload ->
                val session = UserSession(AccessToken(payload.accessToken, "", 0))
                userSession = session
                authCallback?.success(session)
            }
        }*/
    }


    private fun <T>executeCall(apiCall: VKIDCall<T>): Result<T>  {
        activeCalls.add(WeakReference(apiCall))
        return apiCall.execute()
    }

    private var lifecycleCallback: Application.ActivityLifecycleCallbacks? = null
    private fun registerLifeCycleCallback(app: Application, activity: Activity) {
        lifecycleCallback = object: Application.ActivityLifecycleCallbacks {
            private val activityRef = WeakReference(activity)
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {
                if (activityRef.get() == activity && activity.isFinishing) {
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

    public interface AuthCallback {
        @WorkerThread
        public fun success(session: UserSession)
        @WorkerThread
        public fun error(t: Throwable)
        @WorkerThread
        public fun canceled()
    }
}
