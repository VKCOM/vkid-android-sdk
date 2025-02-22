@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.auth.web

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.auth.AuthEventBridge
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.AuthResult
import com.vk.id.internal.auth.VKIDAuthProvider
import com.vk.id.internal.auth.toAuthUriBrowser
import com.vk.id.internal.context.InternalVKIDActivityStarter
import com.vk.id.internal.context.InternalVKIDPackageManager
import com.vk.id.logger.internalVKIDCreateLoggerForClass

internal class WebAuthProvider(
    private val context: InternalVKIDPackageManager,
    private val starter: InternalVKIDActivityStarter
) : VKIDAuthProvider {
    private val logger = internalVKIDCreateLoggerForClass()

    override fun auth(
        authOptions: AuthOptions
    ) {
        val uri = authOptions.toAuthUriBrowser()

        val bestBrowser = WhiteListedBrowserHelper.selectBestBrowser(context)
        if (bestBrowser == null) {
            sendNoBrowserAuthEvent(null)
            return
        }
        logger.debug("Auth with browser ${bestBrowser.packageName}")
        val authIntent = if (bestBrowser.useCustomTab) {
            CustomTabsIntent.Builder().build().intent
        } else {
            Intent(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
        }

        authIntent.data = uri
        authIntent.setPackage(bestBrowser.packageName)

        try {
            starter.startActivity(authIntent)
        } catch (e: ActivityNotFoundException) {
            sendNoBrowserAuthEvent(e)
        }
    }

    private fun sendNoBrowserAuthEvent(throwable: Throwable?) {
        logger.error("Can't start browser to auth", throwable)
        AuthEventBridge.onAuthResult(
            AuthResult.NoBrowserAvailable(
                "Error. Make sure you have a browser installed.",
                throwable
            )
        )
    }
}
