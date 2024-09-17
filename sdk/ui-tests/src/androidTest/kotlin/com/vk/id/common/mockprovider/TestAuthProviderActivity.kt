package com.vk.id.common.mockprovider

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

internal class TestAuthProviderActivity : ComponentActivity() {

    internal companion object {
        var uriReceivedCallback: (Uri?) -> Unit = {}
        var mockAuthProviderConfig = MockAuthProviderConfig()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uriReceivedCallback(intent.data)
        setContent {
            Button(
                modifier = Modifier.testTag("mock_auth_continue"),
                onClick = {
                    returnResult()
                    finish()
                }
            ) {}
        }
    }

    private fun returnResult() {
        // parse intent from sdk
        val data = intent.data ?: return
        val redirectUri = data.getQueryParameter("redirect_uri")
        val state = data.getQueryParameter("state")
        val deviceId = data.getQueryParameter("device_id")

        // check required preconditions
        if (mockAuthProviderConfig.requireUnsetUseAuthProviderIfPossible) {
            if (data.getQueryParameter("screen") == null) {
                return
            }
        }

        // form answer intent from provider
        val resultUriBuilder = Uri.parse(redirectUri).buildUpon()

        if (mockAuthProviderConfig.overrideOAuthToNull) {
            // no code or no state => oauth is null
            resultUriBuilder.appendQueryParameter("device_id", mockAuthProviderConfig.deviceId ?: deviceId)
            resultUriBuilder.build()
        } else if (mockAuthProviderConfig.deviceIdIsNull) {
            resultUriBuilder.appendQueryParameter("state", mockAuthProviderConfig.overrideState ?: state)
            resultUriBuilder.appendQueryParameter("code", code)
        } else {
            // all good
            resultUriBuilder.appendQueryParameter("state", mockAuthProviderConfig.overrideState ?: state)
            resultUriBuilder.appendQueryParameter("device_id", mockAuthProviderConfig.deviceId ?: deviceId)
            resultUriBuilder.appendQueryParameter("code", code)
        }

        startActivity(Intent(Intent.ACTION_VIEW, resultUriBuilder.build()))
    }

    private val code get() = "d654574949e8664ba1"
}
