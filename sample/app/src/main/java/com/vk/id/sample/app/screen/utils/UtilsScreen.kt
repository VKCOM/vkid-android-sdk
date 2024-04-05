package com.vk.id.sample.app.screen.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.vk.id.VKIDAuthFail
import com.vk.id.VKIDUser
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.exchangetoken.VKIDExchangeTokenFail
import com.vk.id.exchangetoken.VKIDExchangeTokenParams
import com.vk.id.exchangetoken.VKIDExchangeTokenToV2Callback
import com.vk.id.logout.VKIDLogoutCallback
import com.vk.id.logout.VKIDLogoutFail
import com.vk.id.logout.VKIDLogoutParams
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import com.vk.id.refresh.VKIDRefreshTokenParams
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserFail
import com.vk.id.refreshuser.VKIDGetUserParams
import com.vk.id.sample.app.screen.Button
import com.vk.id.sample.app.screen.UseToken
import com.vk.id.sample.app.uikit.expandablecard.ExpandableCard
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
        Spacer(modifier = Modifier.height(8.dp))
        AuthUtil()
        Spacer(modifier = Modifier.height(8.dp))
        RefreshTokenUtil()
        Spacer(modifier = Modifier.height(8.dp))
        ExchangeTokenUtil()
        Spacer(modifier = Modifier.height(8.dp))
        LogoutUtil()
        Spacer(modifier = Modifier.height(8.dp))
        RefreshUserUtil()
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun AuthUtil() {
    ExpandableCard(title = "Auth") {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        var currentToken: AccessToken? by remember { mutableStateOf(null) }
        var currentAuthCode: AuthCodeData? by remember { mutableStateOf(null) }
        var state by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state,
            onValueChange = { state = it },
            label = { Text("State (Optional)") },
        )
        var codeChallenge by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = codeChallenge,
            onValueChange = { codeChallenge = it },
            label = { Text("Code challenge (Optional)") },
        )
        Button("Auth") {
            coroutineScope.launch {
                context.vkid.authorize(
                    authCallback = object : VKIDAuthCallback {
                        override fun onSuccess(accessToken: AccessToken) {
                            currentToken = accessToken
                            onVKIDAuthSuccess(context, null, accessToken)
                        }

                        override fun onAuthCode(data: AuthCodeData) {
                            currentAuthCode = data
                        }

                        override fun onFail(fail: VKIDAuthFail) {
                            showToast(context, "Refreshing failed with: ${fail.description}")
                        }
                    },
                    authParams = VKIDAuthParams {
                        this.state = state.takeIf { it.isNotBlank() }
                        this.codeChallenge = codeChallenge.takeIf { it.isNotBlank() }
                    }
                )
            }
        }
        currentAuthCode?.let {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = currentAuthCode?.code.orEmpty(),
                onValueChange = {},
                label = { Text("Auth code") },
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        currentToken?.let { UseToken(it) }
    }
}

@Composable
private fun RefreshTokenUtil() {
    ExpandableCard(title = "Refresh token") {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        var currentToken: AccessToken? by remember { mutableStateOf(null) }
        var state by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state,
            onValueChange = { state = it },
            label = { Text("State (Optional)") },
        )
        var userFetchingState by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = userFetchingState,
            onValueChange = { userFetchingState = it },
            label = { Text("User fetching state (Optional)") },
        )
        Button("Refresh") {
            coroutineScope.launch {
                context.vkid.refreshToken(
                    callback = object : VKIDRefreshTokenCallback {
                        override fun onSuccess(token: AccessToken) {
                            currentToken = token
                            onVKIDAuthSuccess(context, null, token)
                        }

                        override fun onFail(fail: VKIDRefreshTokenFail) {
                            showToast(context, "Refreshing failed with: ${fail.description}")
                        }
                    },
                    params = VKIDRefreshTokenParams {
                        this.state = state.takeIf { it.isNotBlank() }
                        this.userFetchingState = userFetchingState.takeIf { it.isNotBlank() }
                    }
                )
            }
        }
        currentToken?.let { UseToken(it) }
    }
}

@Composable
private fun ExchangeTokenUtil() {
    ExpandableCard(title = "Exchange v1 token to v2") {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        var currentToken: AccessToken? by remember { mutableStateOf(null) }
        var currentAuthCode: AuthCodeData? by remember { mutableStateOf(null) }
        var v1Token by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = v1Token,
            onValueChange = { v1Token = it },
            label = { Text("V1 token") },
        )
        var state by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state,
            onValueChange = { state = it },
            label = { Text("State (Optional)") },
        )
        var codeChallenge by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = codeChallenge,
            onValueChange = { codeChallenge = it },
            label = { Text("Code challenge (Optional)") },
        )
        Button(text = "Exchange") {
            coroutineScope.launch {
                context.vkid.exchangeTokenToV2(
                    v1Token = v1Token,
                    callback = object : VKIDExchangeTokenToV2Callback {
                        override fun onSuccess(token: AccessToken) {
                            currentToken = token
                            onVKIDAuthSuccess(context, null, token)
                        }

                        override fun onAuthCode(data: AuthCodeData) {
                            currentAuthCode = data
                        }

                        override fun onFail(fail: VKIDExchangeTokenFail) {
                            showToast(context, "Exchange failed with: ${fail.description}")
                        }
                    },
                    params = VKIDExchangeTokenParams {
                        this.state = state.takeIf { it.isNotBlank() }
                        this.codeChallenge = codeChallenge.takeIf { it.isNotBlank() }
                    }
                )
            }
        }
        currentAuthCode?.let {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = currentAuthCode?.code.orEmpty(),
                onValueChange = {},
                label = { Text("Auth code") },
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        currentToken?.let { UseToken(it) }
    }
}

@Composable
private fun LogoutUtil() {
    ExpandableCard(title = "Logout") {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        Button(text = "Logout") {
            coroutineScope.launch {
                context.vkid.logout(
                    callback = object : VKIDLogoutCallback {
                        override fun onSuccess() {
                            showToast(context, "Logged out")
                        }

                        override fun onFail(fail: VKIDLogoutFail) {
                            showToast(context, "Logout failed with: ${fail.description}")
                        }
                    },
                    params = VKIDLogoutParams {}
                )
            }
        }
    }
}

@Composable
private fun RefreshUserUtil() {
    ExpandableCard(title = "Refresh user data") {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        var currentUser: VKIDUser? by remember { mutableStateOf(null) }
        var state by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state,
            onValueChange = { state = it },
            label = { Text("State (Optional)") },
        )
        var refreshTokenState by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = refreshTokenState,
            onValueChange = { refreshTokenState = it },
            label = { Text("Refresh token state (Optional)") },
        )
        Button(text = "Refresh") {
            coroutineScope.launch {
                context.vkid.getUserData(
                    callback = object : VKIDGetUserCallback {
                        override fun onSuccess(user: VKIDUser) {
                            currentUser = user
                        }

                        override fun onFail(fail: VKIDGetUserFail) {
                            showToast(context, "User refresh failed with: ${fail.description}")
                        }
                    },
                    params = VKIDGetUserParams {
                        this.state = state.takeIf { it.isNotBlank() }
                        this.refreshTokenState = refreshTokenState.takeIf { it.isNotBlank() }
                    },
                )
            }
        }
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
    }
}
