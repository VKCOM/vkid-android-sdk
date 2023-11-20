package com.vk.id.sample.screen.multibranding.util

import android.content.Context
import com.vk.id.multibranding.OAuthListWidgetAuthCallback
import com.vk.id.sample.screen.styling.util.formatToken
import com.vk.id.sample.screen.styling.util.showToast

internal fun getOAuthListCallback(
    context: Context
) = OAuthListWidgetAuthCallback.WithOAuth { oAuth, token ->
    showToast(
        context,
        "Auth from $oAuth with token ${formatToken(token)}"
    )
}
