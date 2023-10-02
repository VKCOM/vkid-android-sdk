package com.vk.id.internal.auth

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import com.vk.id.internal.auth.web.ContextUtils.addNewTaskFlag
import org.json.JSONException
import org.json.JSONObject
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

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

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)

        authIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            savedInstanceState?.getParcelable(KEY_AUTH_INTENT, Intent::class.java)
        } else {
            @Suppress("DEPRECATION")
            savedInstanceState?.getParcelable(KEY_AUTH_INTENT)
        }

        isWaitingForAuthResult = savedInstanceState?.getBoolean(KEY_WAITING_FOR_AUTH_RESULT, false) ?: false

        processIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        processIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        if (isWaitingForAuthResult && !authWasStarted) {
            // We're waiting for auth result but user returns to activity. Okay. Just finish it.
            AuthEventBridge.onAuthResult(AuthResult.Canceled("User returns to auth activity without auth"))
            finish()
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
        val payload = uri.getQueryParameter("payload") ?: ""
        val payloadJson = try {
            JSONObject(payload)
        } catch (e: JSONException) {
            return AuthResult.AuthActiviyResultFailed("AuthActivity opened with invalid payload json", e)
        }
        return handlePayloadJson(payloadJson)
    }

    private fun handlePayloadJson(
        payloadJson: JSONObject
    ): AuthResult {
        val uuid = payloadJson.optString("uuid")
        val expireTime = payloadJson.optLong("ttl", 0).toExpireTime
        val token = payloadJson.optString("token")
        val user = payloadJson.optJSONObject("user")
        val oauth = payloadJson.optJSONObject("oauth")
        val code = oauth?.optString("code") ?: ""
        val state = oauth?.optString("state") ?: ""

        return AuthResult.Success(
            token = token,
            uuid = uuid,
            expireTime = expireTime,
            userId = user?.optLong("id") ?: 0,
            firstName = user?.optString("first_name") ?: "",
            lastName = user?.optString("last_name") ?: "",
            avatar = user?.optString("avatar"),
            phone = user?.optString("phone"),
            oauth = oauth?.let { AuthResult.OAuth(code, state, "") }
        )
    }

    override fun onPause() {
        super.onPause()
        authWasStarted = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_WAITING_FOR_AUTH_RESULT, isWaitingForAuthResult)
        outState.putParcelable(KEY_AUTH_INTENT, authIntent)
    }

    override fun finish() {
        super.finish()
        setResult(RESULT_OK)
        overridePendingTransition(0, 0)
    }

    companion object {
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
        // todo not reliable?
        // return intent?.data?.scheme?.startsWith("vk") ?: false
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
            startActivity(
                authIntent,
                ActivityOptionsCompat.makeCustomAnimation(
                    this,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                ).toBundle()
            )
            true
        } catch (e: ActivityNotFoundException) {
            false
        }
    }
}
