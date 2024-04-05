@file:OptIn(InternalVKIDApi::class)

package com.vk.id

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.exchangetoken.VKIDExchangeTokenFail
import com.vk.id.exchangetoken.VKIDExchangeTokenParams
import com.vk.id.exchangetoken.VKIDExchangeTokenToV2Callback
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
import com.vk.id.logout.VKIDLoggerOut
import com.vk.id.logout.VKIDLogoutCallback
import com.vk.id.logout.VKIDLogoutParams
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenParams
import com.vk.id.refresh.VKIDTokenRefresher
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserParams
import com.vk.id.refreshuser.VKIDUserRefresher
import com.vk.id.storage.TokenStorage
import com.vk.id.test.ImmediateVKIDApi
import com.vk.id.test.MockAuthProviderChooser
import com.vk.id.test.MockAuthProviderConfig
import com.vk.id.test.OverrideVKIDApi
import com.vk.id.test.TestSilentAuthInfoProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * VKID is the main entry point for integrating VK ID authentication into an Android application.
 * Check readme for more information about integration steps https://github.com/VKCOM/vkid-android-sdk#readme
 */
@Suppress("TooManyFunctions")
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
        this.userRefresher = deps.userRefresher
        this.loggerOut = deps.loggerOut
        this.tokenStorage = deps.tokenStorage

        logger.info(
            "VKID initialized\nVersion name: ${BuildConfig.VKID_VERSION_NAME}\nCI build: ${BuildConfig.CI_BUILD_NUMBER} ${BuildConfig.CI_BUILD_TYPE}"
        )
    }

    private val requestMutex = Mutex()
    private val logger = createLoggerForClass()

    private val authProvidersChooser: Lazy<AuthProvidersChooser>
    private val authOptionsCreator: AuthOptionsCreator
    private val authCallbacksHolder: AuthCallbacksHolder
    private val authResultHandler: Lazy<AuthResultHandler>
    private val dispatchers: CoroutinesDispatchers
    private val vkSilentAuthInfoProvider: Lazy<SilentAuthInfoProvider>
    private val userDataFetcher: Lazy<UserDataFetcher>
    private val tokenRefresher: Lazy<VKIDTokenRefresher>
    private val userRefresher: Lazy<VKIDUserRefresher>
    private val loggerOut: Lazy<VKIDLoggerOut>
    private val tokenStorage: TokenStorage

    /**
     * Initiates the authorization process.
     *
     * @param lifecycleOwner The [LifecycleOwner] in which the authorization process should be handled.
     * @param callback [VKIDAuthCallback] to handle the result of the authorization process.
     * @param params Optional [VKIDAuthParams] for the authentication process.
     */
    public fun authorize(
        lifecycleOwner: LifecycleOwner,
        callback: VKIDAuthCallback,
        params: VKIDAuthParams = VKIDAuthParams {},
    ) {
        lifecycleOwner.lifecycleScope.launch {
            authorize(callback, params)
        }
    }

    /**
     * Initiates the authorization process in a coroutine scope.
     *
     * @param callback [VKIDAuthCallback] to handle the result of the authorization process.
     * @param params Optional [VKIDAuthParams] for the authentication process.
     */
    public suspend fun authorize(
        callback: VKIDAuthCallback,
        params: VKIDAuthParams = VKIDAuthParams {}
    ) {
        authCallbacksHolder.add(callback)
        val authContext = currentCoroutineContext()

        AuthEventBridge.listener = object : AuthEventBridge.Listener {
            override fun onAuthResult(authResult: AuthResult) {
                CoroutineScope(authContext + Job()).launch {
                    authResultHandler.value.handle(authResult)
                    requestMutex.unlock()
                }
            }
        }

        withContext(dispatchers.io) {
            requestMutex.lock()
            val bestAuthProvider = authProvidersChooser.value.chooseBest(params)
            val fullAuthOptions = authOptionsCreator.create(params)
            bestAuthProvider.auth(fullAuthOptions)
        }
    }

    /**
     * Initiates token refreshing.
     *
     * @param lifecycleOwner The [LifecycleOwner] in which the authorization process should be handled.
     * @param callback [VKIDRefreshTokenCallback] to handle the result of the token refreshing.
     * @param params Optional parameters.
     */
    public fun refreshToken(
        lifecycleOwner: LifecycleOwner,
        callback: VKIDRefreshTokenCallback,
        params: VKIDRefreshTokenParams = VKIDRefreshTokenParams {},
    ) {
        lifecycleOwner.lifecycleScope.launch { refreshToken(callback = callback, params = params) }
    }

    /**
     * Initiates token refreshing.
     *
     * @param callback [VKIDRefreshTokenCallback] to handle the result of the token refreshing.
     * @param params Optional parameters.
     */
    public suspend fun refreshToken(
        callback: VKIDRefreshTokenCallback,
        params: VKIDRefreshTokenParams = VKIDRefreshTokenParams {},
    ) {
        requestMutex.withLock {
            tokenRefresher.value.refresh(callback, params)
        }
    }

    /**
     * Exchanges v1 access token to v2 access token.
     *
     * @param lifecycleOwner The [LifecycleOwner] in which the authorization process should be handled.
     * @param v1Token The token to exchange.
     * @param callback [VKIDExchangeTokenToV2Callback] to handle the result of the token exchange.
     * @param params Optional parameters.
     */
    public fun exchangeTokenToV2(
        lifecycleOwner: LifecycleOwner,
        v1Token: String,
        callback: VKIDExchangeTokenToV2Callback,
        params: VKIDExchangeTokenParams = VKIDExchangeTokenParams {},
    ) {
        lifecycleOwner.lifecycleScope.launch { exchangeTokenToV2(v1Token = v1Token, callback = callback, params = params) }
    }

    /**
     * Exchanges v1 access token to v2 access token.
     *
     * @param v1Token The token to exchange.
     * @param callback [VKIDExchangeTokenToV2Callback] to handle the result of the token exchange.
     * @param params Optional parameters.
     */
    public suspend fun exchangeTokenToV2(
        v1Token: String,
        callback: VKIDExchangeTokenToV2Callback,
        params: VKIDExchangeTokenParams = VKIDExchangeTokenParams {},
    ) {
        authorize(
            callback = object : VKIDAuthCallback {
                override fun onSuccess(accessToken: AccessToken) = callback.onSuccess(accessToken)

                override fun onFail(fail: VKIDAuthFail) {
                    val exchangeFail = when (fail) {
                        is VKIDAuthFail.Canceled -> VKIDExchangeTokenFail.Canceled(fail.description)
                        is VKIDAuthFail.FailedApiCall -> VKIDExchangeTokenFail.FailedApiCall(fail.description, fail.throwable)
                        is VKIDAuthFail.FailedOAuth -> VKIDExchangeTokenFail.FailedOAuth(fail.description)
                        is VKIDAuthFail.FailedOAuthState -> VKIDExchangeTokenFail.FailedOAuthState(fail.description)
                        is VKIDAuthFail.FailedRedirectActivity -> VKIDExchangeTokenFail.FailedRedirectActivity(fail.description, fail.throwable)
                        is VKIDAuthFail.NoBrowserAvailable -> VKIDExchangeTokenFail.NoBrowserAvailable(fail.description, fail.throwable)
                    }
                    callback.onFail(exchangeFail)
                }

                override fun onAuthCode(data: AuthCodeData, isCompletion: Boolean) = callback.onAuthCode(data, isCompletion)
            },
            params = VKIDAuthParams {
                token = v1Token
                state = params.state
                codeChallenge = params.codeChallenge
            }
        )
    }

    /**
     * Fetches up-to-data user data using token from previous auth.
     *
     * @param lifecycleOwner The [LifecycleOwner] in which the user data refreshing should be handled.
     * @param callback [VKIDGetUserCallback] to handle the result of the user data refreshing.
     * @param params Optional parameters.
     */
    public fun getUserData(
        lifecycleOwner: LifecycleOwner,
        callback: VKIDGetUserCallback,
        params: VKIDGetUserParams = VKIDGetUserParams {},
    ) {
        lifecycleOwner.lifecycleScope.launch { getUserData(callback = callback, params = params) }
    }

    /**
     * Fetches up-to-data user data using token from previous auth.
     *
     * @param callback [VKIDGetUserCallback] to handle the result of the user data refreshing.
     * @param params Optional parameters.
     */
    public suspend fun getUserData(
        callback: VKIDGetUserCallback,
        params: VKIDGetUserParams = VKIDGetUserParams {},
    ) {
        requestMutex.withLock {
            userRefresher.value.refresh(callback = callback, params = params)
        }
    }

    /**
     * Logs out user and invalidates the access token.
     *
     * @param lifecycleOwner The [LifecycleOwner] in which the logging out should be handled.
     * @param callback [VKIDLogoutCallback] to handle the result of logging out.
     * @param params Optional parameters.
     */
    public fun logout(
        callback: VKIDLogoutCallback,
        lifecycleOwner: LifecycleOwner,
        params: VKIDLogoutParams = VKIDLogoutParams {},
    ) {
        lifecycleOwner.lifecycleScope.launch { logout(callback = callback, params = params) }
    }

    /**
     * Logs out user and invalidates the access token.
     *
     * @param callback [VKIDLogoutCallback] to handle the result of logging out.
     * @param params Optional parameters.
     */
    public suspend fun logout(
        callback: VKIDLogoutCallback,
        params: VKIDLogoutParams = VKIDLogoutParams {},
    ) {
        requestMutex.withLock {
            loggerOut.value.logout(callback = callback, params = params)
        }
    }

    /**
     * Returns current access token or null if auth wasn't passed
     */
    public val accessToken: AccessToken?
        get() = tokenStorage.accessToken

    /**
     * Fetches the user data.
     *
     * @return A Result object containing the fetched [VKIDUser] or an error.
     */
    public suspend fun fetchUserData(): Result<VKIDUser?> {
        return Result.success(userDataFetcher.value.fetchUserData())
    }
}
