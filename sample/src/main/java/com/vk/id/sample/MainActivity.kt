package com.vk.id.sample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vk.id.UserSession
import com.vk.id.VKID

class MainActivity : ComponentActivity() {

    private lateinit var vkid: VKID

    private val vkAuthCallback = object : VKID.AuthCallback {
        override fun success(session: UserSession) {
            val token = session.accessToken.token.hideLastCharacters(10)
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this@MainActivity, "There is token: $token", Toast.LENGTH_LONG).show()
            }
        }

        override fun error(errorMessage: String, error: Throwable?) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this@MainActivity, "Something wrong: $errorMessage", Toast.LENGTH_LONG).show()
            }
        }

        override fun canceled() {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        VKID.logsEnabled = true
        vkid = VKID {
            context = this@MainActivity
            clientId = "7915193"
            clientSecret = "8viIJBplIqqBP2X4rrd5"
            redirectUri = "vk7915193://vk.com"
        }
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    vkid.authorize(this@MainActivity, vkAuthCallback)
                }) {
                    Text("Login")
                }
            }
        }
    }

    private fun String.hideLastCharacters(firstCharactersToKeepVisible: Int): String {
        return if (this.length <= firstCharactersToKeepVisible) {
            this
        } else {
            this.substring(0, firstCharactersToKeepVisible) + "..."
        }
    }
}