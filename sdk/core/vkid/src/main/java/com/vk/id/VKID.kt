@file:OptIn(InternalVKIDApi::class)

package com.vk.id

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.exchangetoken.VKIDExchangeTokenToV2Callback
import com.vk.id.exchangetoken.VKIDTokenExchanger
import com.vk.id.internal.auth.AuthCallbacksHolder
import com.vk.id.internal.auth.AuthEventBridge
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.AuthResult
import com.vk.id.internal.concurrent.CoroutinesDispatchers
import com.vk.id.internal.di.VKIDDeps
import com.vk.id.internal.di.VKIDDepsProd
import com.vk.id.internal.ipc.SilentAuthInfoProvider
import com.vk.id.internal.log.AndroidLogcatLogEngine
import com.vk.id.internal.log.FakeLogEngine
import com.vk.id.internal.log.LogEngine
import com.vk.id.internal.log.VKIDLog
import com.vk.id.internal.log.createLoggerForClass
import com.vk.id.internal.user.UserDataFetcher
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDTokenRefresher
import com.vk.id.test.ImmediateVKIDApi
import com.vk.id.test.MockAuthProviderChooser
import com.vk.id.test.MockAuthProviderConfig
import com.vk.id.test.OverrideVKIDApi
import com.vk.id.test.TestSilentAuthInfoProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * VKID is the main entry point for integrating VK ID authentication into an Android application.
 * Check readme for more information about integration steps https://github.com/VKCOM/vkid-android-sdk#readme
 */
public class VKID {
    /**
     * Constructs a new instance of VKID.
     *
     * @param context The context of the application.
     */
    public constructor(context: Context) : this(VKIDDepsProd(context))

    internal constructor(
        context: Context,
        mockApi: OverrideVKIDApi,
        mockAuthProviderConfig: MockAuthProviderConfig
    ) : this(object : VKIDDepsProd(context) {
        override val authProvidersChooser = lazy { MockAuthProviderChooser(context, mockAuthProviderConfig) }
        override val api = lazy { ImmediateVKIDApi(mockApi) }
        override val vkSilentAuthInfoProvider = lazy { TestSilentAuthInfoProvider() }
    })

    /** @suppress */
    public companion object {
        /**
         * The logging engine used by VKID.
         * Set this property to change the logging implementation.
         *
         * @property logEngine Instance of [LogEngine] to be used for logging.
         */
        @Suppress("MemberVisibilityCanBePrivate")
        public var logEngine: LogEngine = AndroidLogcatLogEngine()
            set(value) {
                field = value
                VKIDLog.setLogEngine(value)
            }

        /**
         * Flag to enable or disable logging in VKID.
         * When set to true, enables logging using the current logEngine.
         * When set to false, disables logging.
         *
         * @property logsEnabled Boolean flag to enable or disable logging.
         */
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
        this.authProvidersChooser = deps.authProvidersChooser
        this.authOptionsCreator = deps.authOptionsCreator
        this.authCallbacksHolder = deps.authCallbacksHolder
        this.authResultHandler = deps.authResultHandler
        this.dispatchers = deps.dispatchers
        this.vkSilentAuthInfoProvider = deps.vkSilentAuthInfoProvider
        this.userDataFetcher = deps.userDataFetcher
        this.tokenRefresher = deps.tokenRefresher
        this.tokenExchanger = deps.tokenExchanger

        logger.info(
            "VKID initialized\nVersion name: ${BuildConfig.VKID_VERSION_NAME}\nCI build: ${BuildConfig.CI_BUILD_NUMBER} ${BuildConfig.CI_BUILD_TYPE}"
        )
    }

    private val logger = createLoggerForClass()

    private val authProvidersChooser: Lazy<AuthProvidersChooser>
    private val authOptionsCreator: AuthOptionsCreator
    private val authCallbacksHolder: AuthCallbacksHolder
    private val authResultHandler: Lazy<AuthResultHandler>
    private val dispatchers: CoroutinesDispatchers
    private val vkSilentAuthInfoProvider: Lazy<SilentAuthInfoProvider>
    private val userDataFetcher: Lazy<UserDataFetcher>
    private val tokenRefresher: Lazy<VKIDTokenRefresher>
    private val tokenExchanger: Lazy<VKIDTokenExchanger>

    /**
     * Initiates the authorization process.
     *
     * @param lifecycleOwner The [LifecycleOwner] in which the authorization process should be handled.
     * @param authCallback [AuthCallback] to handle the result of the authorization process.
     * @param authParams Optional [VKIDAuthParams] for the authentication process.
     */
    public fun authorize(
        lifecycleOwner: LifecycleOwner,
        authCallback: AuthCallback,
        authParams: VKIDAuthParams = VKIDAuthParams {},
    ) {
        lifecycleOwner.lifecycleScope.launch {
            authorize(authCallback, authParams)
        }
    }

    /**
     * Initiates the authorization process in a coroutine scope.
     *
     * @param authCallback [AuthCallback] to handle the result of the authorization process.
     * @param authParams Optional [VKIDAuthParams] for the authentication process.
     */
    public suspend fun authorize(
        authCallback: AuthCallback,
        authParams: VKIDAuthParams = VKIDAuthParams {}
    ) {
        authCallbacksHolder.add(authCallback)
        val authContext = currentCoroutineContext()

        AuthEventBridge.listener = object : AuthEventBridge.Listener {
            override fun onAuthResult(authResult: AuthResult) {
                CoroutineScope(authContext + Job()).launch {
                    authResultHandler.value.handle(authResult)
                }
            }
        }

        withContext(dispatchers.io) {
            val bestAuthProvider = authProvidersChooser.value.chooseBest(authParams)
            val fullAuthOptions = authOptionsCreator.create(authParams)
            bestAuthProvider.auth(fullAuthOptions)
        }
    }

    /**
     * Initiates token refreshing.
     *
     * @param lifecycleOwner The [LifecycleOwner] in which the authorization process should be handled.
     * @param callback [VKIDRefreshTokenCallback] to handle the result of the token refreshing.
     */
    public fun refreshToken(
        lifecycleOwner: LifecycleOwner,
        callback: VKIDRefreshTokenCallback,
    ) {
        lifecycleOwner.lifecycleScope.launch { refreshToken(callback) }
    }

    /**
     * Initiates token refreshing.
     *
     * @param callback [VKIDRefreshTokenCallback] to handle the result of the token refreshing.
     */
    public suspend fun refreshToken(callback: VKIDRefreshTokenCallback) {
        withContext(dispatchers.io) {
            tokenRefresher.value.refresh(callback)
        }
    }

    /**
     * Exchanges v1 access token to v2 access token.
     *
     * @param lifecycleOwner The [LifecycleOwner] in which the authorization process should be handled.
     * @param v1Token The token to exchange.
     * @param callback [VKIDExchangeTokenToV2Callback] to handle the result of the token exchange.
     */
    public fun exchangeTokenToV2(
        lifecycleOwner: LifecycleOwner,
        v1Token: String,
        callback: VKIDExchangeTokenToV2Callback
    ) {
        lifecycleOwner.lifecycleScope.launch { exchangeTokenToV2(v1Token = v1Token, callback = callback) }
    }

    /**
     * Exchanges v1 access token to v2 access token.
     *
     * @param v1Token The token to exchange.
     * @param callback [VKIDExchangeTokenToV2Callback] to handle the result of the token exchange.
     */
    public suspend fun exchangeTokenToV2(
        v1Token: String,
        callback: VKIDExchangeTokenToV2Callback
    ) {
        withContext(dispatchers.io) {
            tokenExchanger.value.exchange(v1Token = v1Token, callback = callback)
        }
    }

    /**
     * Fetches the user data.
     *
     * @return A Result object containing the fetched [VKIDUser] or an error.
     */
    public suspend fun fetchUserData(): Result<VKIDUser?> {
        return Result.success(userDataFetcher.value.fetchUserData())
    }

    /**
     * Callback interface for handling the authentication result.
     */
    public interface AuthCallback {
        /**
         * Called upon successful auth.
         */
        public fun onSuccess(accessToken: AccessToken)

        /**
         * Called upon any failure during auth.
         */
        public fun onFail(fail: VKIDAuthFail)
    }
}
