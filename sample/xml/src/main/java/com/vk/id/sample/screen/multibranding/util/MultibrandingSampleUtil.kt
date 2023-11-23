package com.vk.id.sample.screen.multibranding.util

import android.content.Context
import com.vk.id.multibranding.common.OAuthListWidgetAuthCallback
import com.vk.id.sample.uikit.common.formatToken
import com.vk.id.sample.uikit.common.showToast

public fun getOAuthListCallback(
    context: Context
): OAuthListWidgetAuthCallback.WithOAuth = OAuthListWidgetAuthCallback.WithOAuth { oAuth, accessToken ->
    showToast(
        context,
        "Auth from $oAuth with token ${formatToken(accessToken.token)}"
    )
}
