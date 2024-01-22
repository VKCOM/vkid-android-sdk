package com.vk.id.test

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.vk.id.VKIDUser
import java.net.URLEncoder

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
        val uri = """$redirectUri/?payload=$payload""".trimIndent()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    }

    private val payload get() = """
        {
        "type":"oauth",
        "auth":1,
        "user":{
            "id":30320723,
            "first_name":"${user?.firstName ?: "Some first name"}",
            "last_name":"${user?.lastName ?: "Some last name"}",
            "avatar":"${user?.photo200 ?: "Some avatar"}",
            "avatar_base":null,
            "phone":"Some phone"
        },
        "ttl":$expireTime,
        "uuid":"$uuid",
        "oauth":$oAuth
    }
    """.trimIndent().urlEncode()

    private val oAuth get() = if (!overrideOAuthToNull) {
        """
        {
            "code":"d654574949e8664ba1",
            "state":"$state",
            "oauth_response_type":"code",
            "oauth_code_challenge":"",
            "oauth_code_challenge_method":""
        }
        """.trimIndent()
    } else {
        null
    }

    private val user get() = intent.getParcelableExtra<VKIDUser>("user")
    private val redirectUri get() = intent.getStringExtra("redirectUri")
    private val overrideOAuthToNull get() = intent.getBooleanExtra("overrideOAuthToNull", true)
    private val uuid get() = intent.getStringExtra("deviceId")
    private val state get() = intent.getStringExtra("state")
    private val expireTime = 1000 * 1000 // 1000 Seconds

    private fun String.urlEncode() = URLEncoder.encode(this, Charsets.UTF_8.name())
}
