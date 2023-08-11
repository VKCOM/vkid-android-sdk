package com.vk.id.internal.auth.browser

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import com.vk.id.internal.auth.AuthEventBridge
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.VKIDAuthProvider
import com.vk.id.internal.auth.browser.ContextUtils.addNewTaskFlag
import com.vk.id.internal.auth.toAuthUriBrowser
import com.vk.id.internal.log.createLoggerForClass

internal class AuthBrowser : VKIDAuthProvider {
    private val logger = createLoggerForClass()

    override fun auth(activity: Activity, authOptions: AuthOptions) {
        val uri = authOptions.toAuthUriBrowser()

        val bestBrowser = WhiteListedBrowserHelper.selectBestBrowser(activity)
        if (bestBrowser == null) {
            sendNoBrowserAuthEvent(null)
            return
        }
        logger.debug("Auth with browser ${bestBrowser.packageName}")
        val authIntent = if (bestBrowser.useCustomTab) {
            CustomTabsIntent.Builder()
                .setShowTitle(true)
                .addDefaultShareMenuItem()
                .enableUrlBarHiding()
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
        logger.error("Can't start browser to auth", throwable)
        AuthEventBridge.error("Error. Make sure you have a browser installed.", throwable)
    }
}
