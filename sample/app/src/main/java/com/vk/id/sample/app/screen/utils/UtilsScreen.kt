@file:OptIn(InternalVKIDApi::class)

package com.vk.id.sample.app.screen.utils

import android.content.ComponentName
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.ComponentInfoFlags
import android.os.Build
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
import com.vk.id.auth.Prompt
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.exchangetoken.VKIDExchangeTokenCallback
import com.vk.id.exchangetoken.VKIDExchangeTokenFail
import com.vk.id.exchangetoken.VKIDExchangeTokenParams
import com.vk.id.logout.VKIDLogoutCallback
import com.vk.id.logout.VKIDLogoutFail
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import com.vk.id.refresh.VKIDRefreshTokenParams
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserFail
import com.vk.id.refreshuser.VKIDGetUserParams
import com.vk.id.sample.app.MainActivity
import com.vk.id.sample.app.screen.Button
import com.vk.id.sample.app.screen.UseToken
import com.vk.id.sample.app.uikit.expandablecard.ExpandableCard
import com.vk.id.sample.app.uikit.selector.DropdownSelector
import com.vk.id.sample.xml.uikit.common.onVKIDAuthSuccess
import com.vk.id.sample.xml.uikit.common.showToast
import com.vk.id.sample.xml.vkid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

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
        GetPublicInfoUtil()
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Suppress("LongMethod")
@Composable
private fun AuthUtil() {
    ExpandableCard(title = "Auth") {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        var currentToken: AccessToken? by remember { mutableStateOf(null) }
        var currentAuthCode: AuthCodeData? by remember { mutableStateOf(null) }
        var scopes by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = scopes,
            onValueChange = { scopes = it },
            label = { Text("Scopes (Space-separated)") },
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
        var prompt by remember { mutableStateOf(Prompt.BLANK) }
        DropdownSelector(
            values = Prompt.entries.associateBy { it.name },
            selectedValue = prompt.name,
            onValueSelected = { prompt = it },
            label = { Text("prompt") },
        )
        Button("Auth") {
            coroutineScope.launch {
                context.vkid.authorize(
                    callback = object : VKIDAuthCallback {
                        override fun onAuth(accessToken: AccessToken) {
                            currentToken = accessToken
                            onVKIDAuthSuccess(context, null, accessToken)
                        }

                        override fun onAuthCode(data: AuthCodeData, isCompletion: Boolean) {
                            currentAuthCode = data
                            if (isCompletion) {
                                showToast(context, "Received auth code")
                            }
                        }

                        override fun onFail(fail: VKIDAuthFail) {
                            showToast(context, "Refreshing failed with: ${fail.description}")
                        }
                    },
                    params = VKIDAuthParams {
                        this.scopes = scopes.split(' ', ',').toSet()
                        this.prompt = prompt
                        if (prompt != Prompt.BLANK) this.useOAuthProviderIfPossible = false
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
                maxLines = 1,
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
        var refreshAccessToken by remember { mutableStateOf(true) }
        DropdownSelector(
            values = mapOf("true" to true, "false" to false),
            selectedValue = refreshAccessToken.toString(),
            onValueSelected = { refreshAccessToken = it },
            label = { Text("Refresh access token") },
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
                        this.refreshAccessToken = refreshAccessToken
                    }
                )
            }
        }
        currentToken?.let { UseToken(it) }
    }
}

@Composable
@Suppress("LongMethod")
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
        var codeExchangeState by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = codeExchangeState,
            onValueChange = { codeExchangeState = it },
            label = { Text("State for code exchanging (Optional)") },
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
                    callback = object : VKIDExchangeTokenCallback {
                        override fun onSuccess(accessToken: AccessToken) {
                            currentToken = accessToken
                            onVKIDAuthSuccess(context, null, accessToken)
                        }

                        override fun onAuthCode(data: AuthCodeData, isCompletion: Boolean) {
                            currentAuthCode = data
                            if (isCompletion) {
                                showToast(context, "Received auth code")
                            }
                        }

                        override fun onFail(fail: VKIDExchangeTokenFail) {
                            showToast(context, "Exchange failed with: ${fail.description}")
                        }
                    },
                    params = VKIDExchangeTokenParams {
                        this.state = state.takeIf { it.isNotBlank() }
                        this.codeExchangeState = codeExchangeState.takeIf { it.isNotBlank() }
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
                maxLines = 1,
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

@Composable
private fun GetPublicInfoUtil() {
    ExpandableCard(title = "Get public info") {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        var currentUser: VKIDUser? by remember { mutableStateOf(null) }
        Button(text = "Get") {
            coroutineScope.launch {
                currentUser = withContext(Dispatchers.IO) {
                    val idToken = context.vkid.accessToken?.idToken ?: return@withContext null
                    val api = OkHttpClient.Builder().build()

                    val componentName = ComponentName(context, MainActivity::class.java)
                    val flags = PackageManager.GET_META_DATA or PackageManager.GET_ACTIVITIES
                    val ai: ActivityInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        context.packageManager.getActivityInfo(componentName, ComponentInfoFlags.of(flags.toLong()))
                    } else {
                        context.packageManager.getActivityInfo(componentName, flags)
                    }
                    val clientId = ai.metaData.getInt("VKIDClientID").toString()
//                    val endpoint = "id.vk.com"
                    val endpoint = "tk-training.id.cs7777.vk.com"
                    val formBody = FormBody.Builder()
                        .add("id_token", idToken)
                        .add("client_id", clientId)
                        .build()
                    val url = "https://$endpoint/oauth2/public_info"
                    val request = Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build()
                    val response = api.newCall(request).execute()
                    val body = requireNotNull(response.body).string()
                    val user = JSONObject(body).getJSONObject("user")
                    VKIDUser(
                        firstName = user.optString("first_name"),
                        lastName = user.optString("last_name"),
                        phone = user.optString("phone"),
                        photo200 = user.optString("avatar"),
                        email = user.optString("email"),
                    )
                }
            }
        }
        currentUser?.let {
            Column(horizontalAlignment = Alignment.Start) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    color = MaterialTheme.colorScheme.onBackground,
                    text = """
                    |First name: ${it.firstName}
                    |Last name: ${it.lastName}
                    |Phone: ${it.phone}
                    |Email: ${it.email}
                    |Avatar: ${it.photo200}
                    """.trimMargin()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
