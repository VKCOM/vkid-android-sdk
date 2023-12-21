package com.vk.id.test

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import java.net.URLEncoder
import androidx.compose.material3.Button
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

// TODO: Move to module or debug/java/com/app_name/package_name
// https://stackoverflow.com/questions/27507079/debug-test-specific-activity-declaration
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

    // TODO: Return user and test that it's the same in the returned AccessToken
    private val payload get() = """
        {
        "type":"oauth",
        "auth":1,
        "user":{
            "id":30320723,
            "first_name":"Daniil",
            "last_name":"K.",
            "avatar":"https:\/\/sun9-3.userapi.com\/s\/v1\/ig2\/hEqer8Lx3oOqWUfBKYO_jqTnWyj331ms9vR0bX8reedL3057kvVV0HAt_M70G8uK4ldLDjS6hA2Ot3EGC5VGb0Uw.jpg?size=200x200&quality=95&crop=3,0,637,637&ava=1",
            "avatar_base":null,
            "phone":"+7 *** *** ** 42"
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

    private val redirectUri get() = intent.getStringExtra("redirectUri")
    private val overrideOAuthToNull get() = intent.getBooleanExtra("overrideOAuthToNull", true)
    private val uuid get() = intent.getStringExtra("deviceId")
    private val state get() = intent.getStringExtra("state")
    private val expireTime = 1000 * 1000 // 1000 Seconds

    private fun String.urlEncode() = URLEncoder.encode(this, Charsets.UTF_8.name())
}
