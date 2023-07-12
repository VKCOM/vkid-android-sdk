package com.vk.id.internal.auth.browser

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import com.vk.id.internal.auth.AuthOptions
import com.vk.id.internal.auth.AuthEventBridge
import com.vk.id.internal.auth.VKIDAuthProvider
import com.vk.id.internal.auth.browser.ContextUtils.addNewTaskFlag
import com.vk.id.internal.auth.toAuthUriBrowser

internal class AuthBrowser : VKIDAuthProvider {

    override fun auth(activity: Activity, authOptions: AuthOptions) {
        val uri = authOptions.toAuthUriBrowser()

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


}
