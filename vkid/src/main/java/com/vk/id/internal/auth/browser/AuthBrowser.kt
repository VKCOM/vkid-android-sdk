package com.vk.id.internal.auth.browser

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.AuthEventBridge
import com.vk.id.internal.auth.VKIDAuthProvider
import com.vk.id.internal.auth.browser.ContextUtils.addNewTaskFlag

internal class AuthBrowser : VKIDAuthProvider {

    override fun auth(activity: Activity, authOptions: AuthOptions) {
        val uri = authOptions.toAuthUri()

        val bestBrowser = WhiteListedBrowserHelper.selectBestBrowser(activity)
        if (bestBrowser == null) {
            sendNoBrowserAuthEvent(null)
            return
        }
        val authIntent = if (bestBrowser.useCustomTab) {
            // todo ui
            //val icon = BitmapUtils.createIconForCustomTabs(
                //context,
                //R.drawable.vk_icon_arrow_left_outline_28,
                //styledContext.resolveColor(R.attr.vk_header_tint)
            //)
            CustomTabsIntent.Builder()
                .setShowTitle(true)
                //.setToolbarColor(styledContext.resolveColor(R.attr.vk_header_background))
                //.setStartAnimations(styledContext, android.R.anim.fade_in, android.R.anim.fade_out)
                .addDefaultShareMenuItem()
                .enableUrlBarHiding()
                //.apply { icon?.let(::setCloseButtonIcon) }
                .build()
                .intent
        } else {
            Intent(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
        }

        authIntent.data = uri
        authIntent.setPackage(bestBrowser.packageName)
        authIntent.addNewTaskFlag(activity)

        try {
            OAuthActivity.startForAuth(activity, authIntent)
        } catch(e: ActivityNotFoundException) {
            sendNoBrowserAuthEvent(e)
        }
    }

    private fun sendNoBrowserAuthEvent(throwable: Throwable?) {
        AuthEventBridge.error("Error. Make sure you have a browser installed.", throwable)
    }

    private fun AuthOptions.toAuthUri(): Uri = Uri.Builder()
        .scheme(SCHEME)
        .authority(AUTHORITY)
        .path(PATH)
        .appendQueryParameter(APP_ID, appId)
        .appendQueryParameter(RESPONSE_TYPE, RESPONSE_TYPE_CODE)
        .appendQueryParameter(REDIRECT_URI, redirectUri)
        .appendQueryParameter(CODE_CHALLENGE_METHOD, codeChallengeMethod)
        .appendQueryParameter(CODE_CHALLENGE, codeChallenge)
        .appendQueryParameter(STATE, state)
        .appendQueryParameter(UUID, deviceId)
        .build()

    internal companion object {
        private const val APP_ID = "app_id"
        private const val AUTHORITY = "id.vk.com"
        private const val PATH = "auth"
        private const val CODE_CHALLENGE = "code_challenge"
        private const val CODE_CHALLENGE_METHOD = "code_challenge_method"
        private const val REDIRECT_URI = "redirect_uri"
        private const val RESPONSE_TYPE = "response_type"
        private const val RESPONSE_TYPE_CODE = "code"
        private const val SCHEME = "https"
        private const val STATE = "state"
        private const val UUID = "uuid"
    }
}