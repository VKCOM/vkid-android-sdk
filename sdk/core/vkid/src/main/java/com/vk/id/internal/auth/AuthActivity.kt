@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.auth

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.app.ActivityOptionsCompat
import com.vk.id.VKID
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.auth.web.ContextUtils.addNewTaskFlag
import com.vk.id.logger.internalVKIDCreateLoggerForClass
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Suppress("TooManyFunctions")
internal class AuthActivity : Activity() {
    /**
     * Flag indicating that auth process was started before onResume (cleared in onPause)
     * It isn't saved in onSaveInstanceState because of this flag is just session flag.
     */
    private var authWasStarted = false

    /**
     * Flag indicating that this or previously destroyed activity is waiting for auth.
     * It's saved in onSaveInstanceState because of system could kill our app after opening
     * the browser and we have to know what state was the last in out app.
     */
    private var isWaitingForAuthResult = false

    private var authIntent: Intent? = null

    private val logger = internalVKIDCreateLoggerForClass()

    private var shouldReportCustomTabsPerformance = true
    private var customTabsServiceConnection: CustomTabsServiceConnection? = null
    private var customTabsSession: CustomTabsSession? = null
    private val callback = object : CustomTabsCallback() {

        override fun onNavigationEvent(navigationEvent: Int, extras: Bundle?) {
            super.onNavigationEvent(navigationEvent, extras)
            if (navigationEvent == NAVIGATION_FINISHED && shouldReportCustomTabsPerformance) {
                VKID.instance.performanceTracker.endTracking(PERFORMANCE_KEY_CUSTOM_TABS)
                shouldReportCustomTabsPerformance = false
            }
        }
    }

    override fun onStop() {
        super.onStop()
        VKID.instance.crashReporter.runReportingCrashes({}) {
            if (customTabsServiceConnection == null) return@runReportingCrashes
            unbindService(customTabsServiceConnection!!)
            customTabsServiceConnection = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        VKID.instance.crashReporter.runReportingCrashes({}) {
            shouldReportCustomTabsPerformance = true
            authIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                savedInstanceState?.getParcelable(KEY_AUTH_INTENT, Intent::class.java)
            } else {
                @Suppress("DEPRECATION")
                savedInstanceState?.getParcelable(KEY_AUTH_INTENT)
            }

            isWaitingForAuthResult =
                savedInstanceState?.getBoolean(KEY_WAITING_FOR_AUTH_RESULT, false) ?: false

            processIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        VKID.instance.crashReporter.runReportingCrashes({}) {
            processIntent(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        VKID.instance.crashReporter.runReportingCrashes({}) {
            if (isWaitingForAuthResult && !authWasStarted) {
                // We're waiting for auth result but user returns to activity. Okay. Just finish it.
                AuthEventBridge.onAuthResult(
                    AuthResult.Canceled("User returns to auth activity without auth")
                )
                finish()
            }
        }
    }

    private fun processIntent(intent: Intent?) {
        if (!isWaitingForAuthResult && isIntentWithAuthRequest(intent)) {
            if (handleStartAuth(intent)) {
                isWaitingForAuthResult = true
                authWasStarted = true
            } else {
                finish()
            }
        } else if (isIntentWithAuthResult(intent)) {
            onOauthResult(intent)
            finish()
            isWaitingForAuthResult = false
        }
    }

    private fun onOauthResult(data: Intent?) {
        if (data == null) {
            return
        }
        val authResult = parseOAuthResult(uri = data.data)
        AuthEventBridge.onAuthResult(authResult)
    }

    private fun parseOAuthResult(uri: Uri?): AuthResult {
        if (uri == null) {
            return AuthResult.AuthActiviyResultFailed("AuthActivity opened with null uri", null)
        }
        return try {
            handlePayload(uri)
        } catch (e: UnsupportedOperationException) {
            AuthResult.AuthActiviyResultFailed(
                "AuthActivity opened with invalid url: $uri",
                e
            )
        }
    }

    private fun handlePayload(uri: Uri): AuthResult {
        val code = uri.getQueryParameter("code")
        val state = uri.getQueryParameter("state")
        val deviceId = uri.getQueryParameter("device_id") ?: return AuthResult.AuthActiviyResultFailed("No device id", null)
        val oauth = if (code != null && state != null) {
            AuthResult.OAuth(code, state)
        } else {
            null
        }
        return AuthResult.Success(
            oauth = oauth,
            deviceId = deviceId,
        )
    }

    override fun onPause() {
        super.onPause()
        VKID.instance.crashReporter.runReportingCrashes({}) {
            authWasStarted = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        VKID.instance.crashReporter.runReportingCrashes({}) {
            outState.putBoolean(KEY_WAITING_FOR_AUTH_RESULT, isWaitingForAuthResult)
            outState.putParcelable(KEY_AUTH_INTENT, authIntent)
        }
    }

    override fun finish() {
        super.finish()
        VKID.instance.crashReporter.runReportingCrashes({}) {
            setResult(RESULT_OK)
            overridePendingTransition(0, 0)
        }
    }

    companion object {
        private const val PERFORMANCE_KEY_CUSTOM_TABS = "CustomTabsAuth"
        private const val KEY_AUTH_INTENT = "KEY_AUTH_INTENT"
        private const val KEY_START_AUTH = "KEY_START_AUTH"
        private const val KEY_WAITING_FOR_AUTH_RESULT = "KEY_WAITING_FOR_AUTH_RESULT"

        /**
         * @throws android.content.ActivityNotFoundException
         */
        internal fun startForAuth(
            context: Context,
            authIntent: Intent,
        ) {
            val intent = Intent(context, AuthActivity::class.java)
                .putExtra(KEY_AUTH_INTENT, authIntent)
                .putExtra(KEY_START_AUTH, true)
            intent.addNewTaskFlag(context)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            context.startActivity(intent)
        }

        internal fun createRedirectUriHandlingIntent(context: Context, responseUri: Uri): Intent {
            val intent = Intent(context, AuthActivity::class.java)
            intent.setData(responseUri)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_SINGLE_TOP)
            return intent
        }
    }

    /**
     * Check this activity was started for auth but not with redirect
     *
     * @param intent intent to check
     *
     * @return true if this intent for auth request, false otherwise
     */
    @OptIn(ExperimentalContracts::class)
    private fun isIntentWithAuthRequest(intent: Intent?): Boolean {
        contract {
            returns(true) implies (intent != null)
        }
        return intent?.getBooleanExtra(KEY_START_AUTH, false) == true
    }

    @OptIn(ExperimentalContracts::class)
    private fun isIntentWithAuthResult(intent: Intent?): Boolean {
        contract {
            returns(true) implies (intent != null)
        }
        return intent?.data?.scheme != null
    }

    private fun handleStartAuth(intent: Intent): Boolean {
        authIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(KEY_AUTH_INTENT, Intent::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(KEY_AUTH_INTENT)
        }
        return try {
            if (authIntent!!.`package` == CustomTabsClient.getPackageName(this, null)) {
                VKID.instance.performanceTracker.startTracking(PERFORMANCE_KEY_CUSTOM_TABS)
                customTabsServiceConnection = object : CustomTabsServiceConnection() {
                    override fun onCustomTabsServiceConnected(
                        name: ComponentName,
                        client: CustomTabsClient
                    ) {
                        VKID.instance.crashReporter.runReportingCrashes({}) {
                            customTabsSession = client.newSession(callback)
                            client.warmup(0)
                            launchAuth(
                                CustomTabsIntent.Builder(customTabsSession)
                                    .setShowTitle(true)
                                    .addDefaultShareMenuItem()
                                    .enableUrlBarHiding()
                                    .build()
                                    .intent.apply { data = authIntent!!.data }
                            )
                        }
                    }

                    override fun onServiceDisconnected(name: ComponentName?) = Unit
                }
                val packageName = CustomTabsClient.getPackageName(this, null)
                if (packageName != null) {
                    CustomTabsClient.bindCustomTabsService(this, packageName, customTabsServiceConnection!!)
                } else {
                    return false
                }
            } else {
                launchAuth(authIntent!!)
            }
            true
        } catch (e: ActivityNotFoundException) {
            logger.error("Can't start auth", e)
            false
        }
    }

    private fun launchAuth(intent: Intent) {
        startActivity(
            intent,
            ActivityOptionsCompat.makeCustomAnimation(
                this,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            ).toBundle()
        )
    }
}
