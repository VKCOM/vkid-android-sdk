package com.vk.id.sample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vk.id.UserSession
import com.vk.id.VKID
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {

    private lateinit var vkid: VKID

    private val vkAuthCallback = object : VKID.AuthCallback {
        override fun success(session: UserSession) {
            setUiStateVkAuthComplete()
            val token = session.accessToken.token.hideLastCharacters(10)
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this@MainActivity, "There is token: $token", Toast.LENGTH_LONG).show()
            }
        }

        override fun error(errorMessage: String, error: Throwable?) {
            setUiStateVkAuthComplete()
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this@MainActivity, "Something wrong: $errorMessage", Toast.LENGTH_LONG).show()
            }
        }

        override fun canceled() {
            setUiStateVkAuthComplete()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        VKID.logsEnabled = true
        vkid = VKID {
            context = this@MainActivity
            clientId = "51726321"
            clientSecret = "6Tn9Z5bht6GL08cBaETv"
            redirectUri = "vk51726321://vk.com"
        }
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val vkInProgress by vkAuthInProgress.collectAsState()
                VKIDButton(
                    inProgress = vkInProgress,
                    enabled = vkInProgress.not(),
                    onClick = ::startAuth,
                    modifier = Modifier.width(335.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                VKIDButtonSmall(
                    inProgress = vkInProgress,
                    enabled = vkInProgress.not(),
                    onClick = ::startAuth
                )
            }
        }
    }

    private fun startAuth() {
        setUiStateVkAuthInProgress()
        vkid.authorize(this@MainActivity, vkAuthCallback)
    }

    private fun String.hideLastCharacters(firstCharactersToKeepVisible: Int): String {
        return if (this.length <= firstCharactersToKeepVisible) {
            this
        } else {
            this.substring(0, firstCharactersToKeepVisible) + "..."
        }
    }

    private fun setUiStateVkAuthComplete() {
        vkAuthInProgress.tryEmit(false)
    }

    private fun setUiStateVkAuthInProgress() {
        vkAuthInProgress.tryEmit(true)
    }

    private val vkAuthInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
}