package com.vk.id.sample.xml.multibranding.util

import android.content.Context
import com.vk.id.AccessToken
import com.vk.id.multibranding.common.callback.OAuthListWidgetAuthCallback
import com.vk.id.sample.xml.uikit.common.formatToken
import com.vk.id.sample.xml.uikit.common.showToast

public fun getOAuthListCallback(
    context: Context,
    onToken: (AccessToken) -> Unit
): OAuthListWidgetAuthCallback.WithOAuth = OAuthListWidgetAuthCallback.WithOAuth { oAuth, accessToken ->
    onToken(accessToken)
    showToast(
        context,
        "Auth from $oAuth with token ${formatToken(accessToken.token)}"
    )
}
