package com.vk.id.sample.app.screen.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vk.id.AccessToken
import com.vk.id.VKIDUser
import com.vk.id.exchangetoken.VKIDExchangeTokenFail
import com.vk.id.exchangetoken.VKIDExchangeTokenToV2Callback
import com.vk.id.logout.VKIDLogoutCallback
import com.vk.id.logout.VKIDLogoutFail
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserFail
import com.vk.id.sample.app.screen.Button
import com.vk.id.sample.app.screen.UseToken
import com.vk.id.sample.xml.uikit.common.onVKIDAuthSuccess
import com.vk.id.sample.xml.uikit.common.showToast
import com.vk.id.sample.xml.vkid
import kotlinx.coroutines.launch

@Composable
internal fun UtilsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RefreshTokenUtil()
        ExchangeTokenUtil()
        LogoutUtil()
        RefreshUserUtil()
    }
}

@Composable
private fun RefreshTokenUtil() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var currentToken: AccessToken? by remember { mutableStateOf(null) }
    Button("Refresh token") {
        coroutineScope.launch {
            context.vkid.refreshToken(object : VKIDRefreshTokenCallback {
                override fun onSuccess(token: AccessToken) {
                    currentToken = token
                    onVKIDAuthSuccess(context, null, token)
                }

                override fun onFail(fail: VKIDRefreshTokenFail) {
                    showToast(context, "Refreshing failed with: ${fail.description}")
                }
            })
        }
    }
    currentToken?.let { UseToken(it) }
}

@Composable
private fun ExchangeTokenUtil() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var currentToken: AccessToken? by remember { mutableStateOf(null) }
    var v1Token by remember { mutableStateOf("") }
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = v1Token,
        onValueChange = { v1Token = it },
        label = { Text("V1 token") },
        modifier = Modifier.width(355.dp),
    )
    Button(text = "Exchange v1 token to v2") {
        coroutineScope.launch {
            context.vkid.exchangeTokenToV2(
                v1Token,
                object : VKIDExchangeTokenToV2Callback {
                    override fun onSuccess(token: AccessToken) {
                        currentToken = token
                        onVKIDAuthSuccess(context, null, token)
                    }

                    override fun onFail(fail: VKIDExchangeTokenFail) {
                        showToast(context, "Exchange failed with: ${fail.description}")
                    }
                }
            )
        }
    }
    currentToken?.let { UseToken(it) }
}

@Composable
private fun LogoutUtil() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Button(text = "Logout") {
        coroutineScope.launch {
            context.vkid.logout(object : VKIDLogoutCallback {
                override fun onSuccess() {
                    showToast(context, "Logged out")
                }

                override fun onFail(fail: VKIDLogoutFail) {
                    showToast(context, "Logout failed with: ${fail.description}")
                }
            })
        }
    }
}

@Composable
private fun RefreshUserUtil() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var currentUser: VKIDUser? by remember { mutableStateOf(null) }
    currentUser?.let {
        Column(horizontalAlignment = Alignment.Start) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                color = MaterialTheme.colorScheme.onBackground,
                text = """|
                    |First name: ${it.firstName}
                    |Last name: ${it.lastName}
                    |Phone: ${it.phone}
                    |Email: ${it.email}
                """.trimMargin()
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
    Button(text = "Refresh user data") {
        coroutineScope.launch {
            context.vkid.getUserData(object : VKIDGetUserCallback {
                override fun onSuccess(user: VKIDUser) {
                    currentUser = user
                }

                override fun onFail(fail: VKIDGetUserFail) {
                    showToast(context, "User refresh failed with: ${fail.description}")
                }
            })
        }
    }
}
