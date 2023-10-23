package com.vk.id.sample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.sample.button.CreateButtonsSample

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        VKID.logsEnabled = true
        setContentView(R.layout.activity_main)
//        setContent {
//            CreateButtonsSample(
//                onSuccess = ::onVKIDAuthSuccess,
//                onFail = ::onVKIDAuthFail
//            )
//        }
    }

    private fun onVKIDAuthSuccess(accessToken: AccessToken) {
        val token = accessToken.token.hideLastCharacters(TOKEN_VISIBLE_CHARACTERS)
        showToast("There is token: $token")
    }

    private fun onVKIDAuthFail(fail: VKIDAuthFail) {
        when (fail) {
            is VKIDAuthFail.Canceled -> {
                showToast("Auth canceled")
            }

            else -> {
                showToast("Something wrong: ${fail.description}")
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

    private fun showToast(text: String) {
        toastOnScreen?.cancel()
        toastOnScreen = Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG)
        toastOnScreen?.show()
    }

    private var toastOnScreen: Toast? = null

    private companion object {
        const val TOKEN_VISIBLE_CHARACTERS = 10
    }
}
