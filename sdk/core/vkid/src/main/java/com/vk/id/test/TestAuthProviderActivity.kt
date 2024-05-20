package com.vk.id.test

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import java.util.UUID

internal class TestAuthProviderActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val uri = """$redirectUri$codeParameter$stateParameter$deviceIdParameter""".trimIndent()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }

    private val redirectUri get() = intent.getStringExtra("redirectUri")
    private val overrideOAuthToNull get() = intent.getBooleanExtra("overrideOAuthToNull", true)
    private val code get() = "d654574949e8664ba1"
    private val codeParameter get() = "&code=$code".takeIf { !overrideOAuthToNull }.orEmpty()
    private val state get() = intent.getStringExtra("state")
    private val stateParameter get() = "&state=$state".takeIf { !overrideOAuthToNull }.orEmpty()
    private val overrideDeviceIdToNull get() = intent.getBooleanExtra("overrideDeviceIdToNull", true)
    private val deviceIdParameter get() = "&device_id=${UUID.randomUUID()}".takeIf { !overrideDeviceIdToNull }.orEmpty()
}
