@file:OptIn(InternalVKIDApi::class)

package com.vk.id

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.vk.id.analytics.LogcatTracker
import com.vk.id.analytics.VKIDAnalytics
import com.vk.id.analytics.stat.StatTracker
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.exchangetoken.VKIDExchangeTokenCallback
import com.vk.id.exchangetoken.VKIDExchangeTokenParams
import com.vk.id.exchangetoken.VKIDTokenExchanger
import com.vk.id.internal.analytics.CustomAuthAnalytics
import com.vk.id.internal.auth.AuthCallbacksHolder
import com.vk.id.internal.auth.AuthEventBridge
import com.vk.id.internal.auth.AuthProvidersChooser
import com.vk.id.internal.auth.AuthResult
import com.vk.id.internal.auth.ServiceCredentials
import com.vk.id.internal.auth.device.InternalVKIDDeviceIdProvider
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import com.vk.id.internal.context.InternalVKIDActivityStarter
import com.vk.id.internal.context.InternalVKIDPackageManager
import com.vk.id.internal.di.VKIDDeps
import com.vk.id.internal.di.VKIDDepsProd
import com.vk.id.internal.ipc.SilentAuthInfoProvider
import com.vk.id.internal.store.InternalVKIDPrefsStore
import com.vk.id.internal.user.UserDataFetcher
import com.vk.id.logger.InternalVKIDAndroidLogcatLogEngine
import com.vk.id.logger.InternalVKIDFakeLogEngine
import com.vk.id.logger.InternalVKIDLog
import com.vk.id.logger.LogEngine
import com.vk.id.logger.internalVKIDCreateLoggerForClass
import com.vk.id.logout.VKIDLoggerOut
import com.vk.id.logout.VKIDLogoutCallback
import com.vk.id.logout.VKIDLogoutParams
import com.vk.id.network.groupsubscription.InternalVKIDGroupSubscriptionApiContract
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenParams
import com.vk.id.refresh.VKIDTokenRefresher
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserParams
import com.vk.id.refreshuser.VKIDUserRefresher
import com.vk.id.storage.InternalVKIDEncryptedSharedPreferencesStorage
import com.vk.id.storage.InternalVKIDTokenStorage
import com.vk.id.test.InternalVKIDImmediateApi
import com.vk.id.test.InternalVKIDOverrideApi
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

    /** @suppress */
    public companion object {

        @Volatile
        private var _instance: VKID? = null

        private fun init(vkid: VKID) {
            synchronized(this) {
                check(BuildConfig.DEBUG || _instance == null) { "You've already initialized VKID" }
                _instance = vkid
            }
        }

        /**
         * Initializes a new instance of VKID.
         * You must not call this method twice.
         *
         * @param context The context of the application.
         */
        public fun init(context: Context): Unit = init(context, isFlutter = false)

        @InternalVKIDApi
        public fun init(context: Context, isFlutter: Boolean): Unit = init(VKID(VKIDDepsProd(context, isFlutter)))

        @Suppress("LongParameterList")
        internal fun init(
            context: Context,
            mockApi: InternalVKIDOverrideApi,
            groupSubscriptionApiContract: InternalVKIDGroupSubscriptionApiContract,
            deviceIdStorage: InternalVKIDDeviceIdProvider.DeviceIdStorage?,
            prefsStore: InternalVKIDPrefsStore?,
            encryptedSharedPreferencesStorage: InternalVKIDEncryptedSharedPreferencesStorage?,
            packageManager: InternalVKIDPackageManager?,
            activityStarter: InternalVKIDActivityStarter?,
        ): Unit = init(
            VKID(object : VKIDDepsProd(context, isFlutter = false) {
                override val api = lazy { InternalVKIDImmediateApi(mockApi) }
                override val groupSubscriptionApiService: Lazy<InternalVKIDGroupSubscriptionApiContract> =
                    lazy { groupSubscriptionApiContract }
                override val vkSilentAuthInfoProvider = lazy { TestSilentAuthInfoProvider() }
                override val deviceIdStorage = lazy { deviceIdStorage ?: super.deviceIdStorage.value }
                override val prefsStore = lazy { prefsStore ?: super.prefsStore.value }
                override val encryptedSharedPreferencesStorage =
                    lazy { encryptedSharedPreferencesStorage ?: super.encryptedSharedPreferencesStorage.value }
                override val vkidPackageManager = packageManager ?: super.vkidPackageManager
                override val activityStarter = activityStarter ?: super.activityStarter
            })
        )

        @InternalVKIDApi
        public fun initForScreenshotTests(context: Context) {
            init(
                VKID(object : VKIDDepsProd(context, isFlutter = false) {
                    override val serviceCredentials: Lazy<ServiceCredentials> = lazy {
                        ServiceCredentials(
                            clientID = "",
                            clientSecret = "",
                            redirectUri = "",
                        )
                    }
                    override val statTracker: VKIDAnalytics.Tracker = object : VKIDAnalytics.Tracker {
                        override fun trackEvent(
                            accessToken: String?,
                            name: String,
                            vararg params: VKIDAnalytics.EventParam
                        ) = Unit
                    }
                })
            )
        }

        /**
         * Returns a VKID Instance.
         * You must call [init] before accessing this property.
         */
        public val instance: VKID
            get() = _instance ?: synchronized(this) { _instance } ?: error("VKID is not initialized")

        /**
         * The logging engine used by VKID.
         * Set this property to change the logging implementation.
         *
         * @property logEngine Instance of [LogEngine] to be used for logging.
         */
        @Suppress("MemberVisibilityCanBePrivate")
        public var logEngine: LogEngine = InternalVKIDAndroidLogcatLogEngine()
            set(value) {
                field = value
                InternalVKIDLog.setLogEngine(value)
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
                    InternalVKIDLog.setLogEngine(logEngine)
                    LogcatTracker().let {
                        analyticsDebugTracker = it
                        VKIDAnalytics.addTracker(it)
                    }
                } else {
                    InternalVKIDLog.setLogEngine(InternalVKIDFakeLogEngine())
                    analyticsDebugTracker?.let {
                        VKIDAnalytics.removeTracker(it)
                    }
                }
            }

        private var analyticsDebugTracker: LogcatTracker? = null
    }

    /**
     * Only for tests, to provide mocked dependencies
     */
    @InternalVKIDApi
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
        this.userRefresher = deps.userRefresher
        this.loggerOut = deps.loggerOut
        this.tokenStorage = deps.tokenStorage
        this.groupSubscriptionApiServiceInternal = deps.groupSubscriptionApiService
        this.clientIdProvider = { deps.serviceCredentials.value.clientID }
        this.context = deps.context

        VKIDAnalytics.addTracker(deps.statTracker)

        logger.info(
            "VKID initialized\nVersion name: ${BuildConfig.VKID_VERSION_NAME}\nCI build: ${BuildConfig.CI_BUILD_NUMBER} ${BuildConfig.CI_BUILD_TYPE}"
        )
        VKIDAnalytics.trackEvent("vkid_sdk_init", VKIDAnalytics.EventParam("wrapper_sdk_type", strValue = if (deps.isFlutter) "flutter" else "none"))
    }

    private val requestMutex = Mutex()
    private val logger = internalVKIDCreateLoggerForClass()

    private val authProvidersChooser: Lazy<AuthProvidersChooser>
    private val authOptionsCreator: AuthOptionsCreator
    private val authCallbacksHolder: AuthCallbacksHolder
    private val authResultHandler: Lazy<AuthResultHandler>
    private val dispatchers: VKIDCoroutinesDispatchers
    private val vkSilentAuthInfoProvider: Lazy<SilentAuthInfoProvider>
    private val userDataFetcher: Lazy<UserDataFetcher>
    private val tokenRefresher: Lazy<VKIDTokenRefresher>
    private val tokenExchanger: Lazy<VKIDTokenExchanger>
    private val userRefresher: Lazy<VKIDUserRefresher>
    private val loggerOut: Lazy<VKIDLoggerOut>
    private val groupSubscriptionApiServiceInternal: Lazy<InternalVKIDGroupSubscriptionApiContract>

    @InternalVKIDApi
    public val tokenStorage: InternalVKIDTokenStorage

    @InternalVKIDApi
    public val groupSubscriptionApiService: InternalVKIDGroupSubscriptionApiContract
        get() = groupSubscriptionApiServiceInternal.value

    private val clientIdProvider: () -> String

    @InternalVKIDApi
    public val clientId: String get() = clientIdProvider()

    @InternalVKIDApi
    public val context: Context

    /**
     * Initiates the authorization process.
     *
     * @param lifecycleOwner The [LifecycleOwner] in which the authorization process should be handled.
     * @param callback [VKIDAuthCallback] to handle the result of the authorization process.
     * @param params Optional [VKIDAuthParams] for the authentication process.
     *
     * Example usage:
     *
     * ```
     * VKID.instance.authorize(
     *     lifecycleOwner = MainActivity@this,
     *     callback = vkAuthCallback,
     *     params = VKIDAuthParams {
     *         scopes = setOf("status", "email")
     *     }
     * )
     * ```
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
     *
     * Example usage:
     *
     * ```
     * VKID.instance.authorize(
     *     callback = vkAuthCallback,
     *     params = VKIDAuthParams {
     *         scopes = setOf("status", "email")
     *     }
     * )
     * ```
     */
    public suspend fun authorize(
        callback: VKIDAuthCallback,
        params: VKIDAuthParams = VKIDAuthParams {}
    ) {
        authCallbacksHolder.add(callback)
        val authContext = currentCoroutineContext()

        val statParams = if (!params.internalUse) {
            CustomAuthAnalytics.customAuthStart(params)
        } else {
            StatParams(
                flowSource = params.extraParams?.get(StatTracker.EXTERNAL_PARAM_FLOW_SOURCE) ?: "",
                sessionId = params.extraParams?.get(StatTracker.EXTERNAL_PARAM_SESSION_ID) ?: ""
            )
        }

        AuthEventBridge.listener = object : AuthEventBridge.Listener {
            override fun onAuthResult(authResult: AuthResult) {
                CoroutineScope(authContext + Job()).launch {
                    authResultHandler.value.handle(authResult, onFail = {
                        if (!params.internalUse) {
                            CustomAuthAnalytics.customAuthError(statParams)
                        }
                    })
                    if (requestMutex.isLocked) {
                        requestMutex.unlock()
                    }
                }
            }
        }

        withContext(dispatchers.io) {
            requestMutex.lock()
            val bestAuthProvider = authProvidersChooser.value.chooseBest(params)
            val fullAuthOptions = authOptionsCreator.create(params, statParams)
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
     * @param callback [VKIDExchangeTokenCallback] to handle the result of the token exchange.
     * @param params Optional parameters.
     */
    public fun exchangeTokenToV2(
        lifecycleOwner: LifecycleOwner,
        v1Token: String,
        callback: VKIDExchangeTokenCallback,
        params: VKIDExchangeTokenParams = VKIDExchangeTokenParams {},
    ) {
        lifecycleOwner.lifecycleScope.launch { exchangeTokenToV2(v1Token = v1Token, callback = callback, params = params) }
    }

    /**
     * Exchanges v1 access token to v2 access token.
     *
     * @param v1Token The token to exchange.
     * @param callback [VKIDExchangeTokenCallback] to handle the result of the token exchange.
     * @param params Optional parameters.
     */
    public suspend fun exchangeTokenToV2(
        v1Token: String,
        callback: VKIDExchangeTokenCallback,
        params: VKIDExchangeTokenParams = VKIDExchangeTokenParams {},
    ) {
        requestMutex.withLock {
            tokenExchanger.value.exchange(v1Token = v1Token, params = params, callback = callback)
        }
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
     * Returns current access token or null if auth wasn't passed.
     */
    public val accessToken: AccessToken?
        get() = tokenStorage.accessToken

    @InternalVKIDApi
    public fun mockAuthorized() {
        tokenStorage.accessToken = AccessToken(
            token = "",
            idToken = null,
            userID = 0,
            expireTime = 0,
            userData = VKIDUser(
                firstName = "",
                lastName = "",
            ),
            scopes = null,
        )
    }

    /**
     * Returns current refresh token or null if auth wasn't passed.
     */
    public val refreshToken: RefreshToken?
        get() = tokenStorage.refreshToken

    /**
     * Fetches the user data.
     *
     * @return A Result object containing the fetched [VKIDUser] or an error.
     */
    public suspend fun fetchUserData(): Result<VKIDUser?> {
        return Result.success(userDataFetcher.value.fetchUserData())
    }
}
