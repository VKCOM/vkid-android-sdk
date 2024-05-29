package com.vk.id.internal.auth

import android.app.Activity
import android.os.Bundle

/**
 * Activity to handle redirectUri
 */
internal class RedirectUriReceiverActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val redirectUri = intent.data
        if (redirectUri != null) {
            startActivity(
                AuthActivity.createRedirectUriHandlingIntent(this, redirectUri)
            )
        }
        finish()
    }
}
