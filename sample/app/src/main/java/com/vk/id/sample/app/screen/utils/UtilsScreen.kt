@file:OptIn(InternalVKIDApi::class)
@file:Suppress("TooManyFunctions")

package com.vk.id.sample.app.screen.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vk.id.AccessToken
import com.vk.id.RefreshToken
import com.vk.id.VKID
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
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSheet
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSnackbarHost
import com.vk.id.group.subscription.compose.ui.rememberGroupSubscriptionSheetState
import com.vk.id.logout.VKIDLogoutCallback
import com.vk.id.logout.VKIDLogoutFail
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import com.vk.id.refresh.VKIDRefreshTokenParams
import com.vk.id.refreshuser.VKIDGetUserCallback
import com.vk.id.refreshuser.VKIDGetUserFail
import com.vk.id.refreshuser.VKIDGetUserParams
import com.vk.id.sample.app.screen.Button
import com.vk.id.sample.app.screen.UseToken
import com.vk.id.sample.app.uikit.expandablecard.ExpandableCard
import com.vk.id.sample.app.uikit.selector.CheckboxSelector
import com.vk.id.sample.app.uikit.selector.DropdownSelector
import com.vk.id.sample.app.util.VkIdNetworkHelper
import com.vk.id.sample.xml.VKIDInitializer
import com.vk.id.sample.xml.prefs.captcha.CaptchaHandler
import com.vk.id.sample.xml.prefs.flutter.IsFlutterHandler
import com.vk.id.sample.xml.prefs.sctrictmode.StrictModeHandler
import com.vk.id.sample.xml.uikit.common.copyToClipboard
import com.vk.id.sample.xml.uikit.common.onVKIDAuthSuccess
import com.vk.id.sample.xml.uikit.common.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
internal fun UtilsScreen(navController: NavController) {
    val snackbarHostState = SnackbarHostState()
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            AuthUtil(snackbarHostState)
            Spacer(modifier = Modifier.height(8.dp))
            RefreshTokenUtil()
            Spacer(modifier = Modifier.height(8.dp))
            ExchangeTokenUtil()
            Spacer(modifier = Modifier.height(8.dp))
            RefreshUserUtil()
            Spacer(modifier = Modifier.height(8.dp))
            GetPublicInfoUtil()
            Spacer(modifier = Modifier.height(8.dp))
            CurrentTokenUtil()
            Spacer(modifier = Modifier.height(8.dp))
            LocaleUtil()
            Spacer(modifier = Modifier.height(8.dp))
            CaptchaUtil()
            Spacer(modifier = Modifier.height(8.dp))
            RevokeUtil()
            Spacer(modifier = Modifier.height(8.dp))
            LogoutUtil()
            Spacer(modifier = Modifier.height(8.dp))
            OldSdkSample(navController)
            Spacer(modifier = Modifier.height(8.dp))
            StrictModeUtil()
            Spacer(modifier = Modifier.height(8.dp))
            IsFlutterUtil()
            Spacer(modifier = Modifier.height(8.dp))
        }
        GroupSubscriptionSnackbarHost(snackbarHostState = snackbarHostState)
    }
}

@Suppress("LongMethod")
@Composable
private fun AuthUtil(
    snackbarHostState: SnackbarHostState
) {
    ExpandableCard(title = "Auth") {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        var currentToken: AccessToken? by remember { mutableStateOf(null) }
        var currentAuthCode: AuthCodeData? by remember { mutableStateOf(null) }
        var scopes by remember { mutableStateOf("") }
        var groupId by remember { mutableStateOf("") }
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
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = groupId,
            onValueChange = { groupId = it },
            label = { Text("Group subscription id") },
        )
        val groupSubscriptionSheetState = rememberGroupSubscriptionSheetState()
        Button("Auth") {
            coroutineScope.launch {
                VKID.instance.authorize(
                    callback = object : VKIDAuthCallback {
                        override fun onAuth(accessToken: AccessToken) {
                            currentToken = accessToken
                            onVKIDAuthSuccess(context, null, accessToken)
                            if (groupId.isNotBlank()) {
                                groupSubscriptionSheetState.show()
                            }
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
                        this.scopes = scopes.split(' ', ',').toSet() + (if (groupId.isNotBlank()) setOf("groups") else emptySet())
                        this.prompt = prompt
                        if (prompt != Prompt.BLANK) this.useOAuthProviderIfPossible = false
                        this.state = state.takeIf { it.isNotBlank() }
                        this.codeChallenge = codeChallenge.takeIf { it.isNotBlank() }
                    }
                )
            }
        }
        GroupSubscriptionSheet(
            modifier = Modifier,
            state = groupSubscriptionSheetState,
            groupId = groupId,
            onSuccess = { showToast(context, "Success") },
            onFail = { showToast(context, "Fail: ${it.description}") },
            snackbarHostState = snackbarHostState,
        )
        currentAuthCode?.let { data ->
            var codeVerifier: String by remember { mutableStateOf("") }
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = codeVerifier,
                onValueChange = { codeVerifier = it },
                label = { Text("Code verifier") },
                maxLines = 1,
            )
            Button(text = "Exchange code") {
                coroutineScope.launch {
                    try {
                        currentToken = VkIdNetworkHelper.exchangeAuthCode(
                            context = context,
                            code = data.code,
                            codeVerifier = codeVerifier,
                            deviceId = data.deviceId
                        )
                    } catch (@Suppress("TooGenericExceptionCaught") t: Throwable) {
                        showToast(context, "Error: ${t.message}")
                    }
                }
            }
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
                VKID.instance.refreshToken(
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
                VKID.instance.exchangeTokenToV2(
                    v1Token = v1Token,
                    callback = object : VKIDExchangeTokenCallback {
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
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Button(text = "Logout", verticalPadding = 0.dp) {
        coroutineScope.launch {
            VKID.instance.logout(
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

@Composable
private fun OldSdkSample(navController: NavController) {
    Button(text = "Old SDK sample", verticalPadding = 0.dp) {
        navController.navigate("old-sdk-sample")
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
                VKID.instance.getUserData(
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
                try {
                    val idToken = VKID.instance.accessToken?.idToken
                    if (idToken != null) {
                        currentUser = VkIdNetworkHelper.getPublicInfo(context, idToken)
                    } else {
                        showToast(context, "No id token available")
                    }
                } catch (@Suppress("TooGenericExceptionCaught") t: Throwable) {
                    showToast(context, "Error: ${t.message}")
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

@Composable
private fun CurrentTokenUtil() {
    ExpandableCard(title = "Current token", contentAlignment = Alignment.CenterHorizontally) {
        var accessToken by remember { mutableStateOf<AccessToken?>(null) }
        var refreshToken by remember { mutableStateOf<RefreshToken?>(null) }
        LaunchedEffect(key1 = Unit) {
            withContext(Dispatchers.IO) {
                accessToken = VKID.instance.accessToken
                refreshToken = VKID.instance.refreshToken
            }
        }
        accessToken?.let {
            UseToken(accessToken = it)
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            Button(text = "Use token to get Birthday") {
                coroutineScope.launch {
                    try {
                        showToast(context, "Birth Day: ${getUserBday(context, it)}")
                    } catch (@Suppress("TooGenericExceptionCaught") t: Throwable) {
                        showToast(context, "Error: ${t.message}")
                    }
                }
            }
            Button(text = "Copy access token to clipboard") {
                copyToClipboard(context, "Access token", it.token)
            }
        } ?: Text(
            modifier = Modifier.defaultMinSize(minHeight = 40.dp),
            text = "You are not authorized"
        )
    }
}

@Composable
private fun LocaleUtil() {
    ExpandableCard(title = "Locale", contentAlignment = Alignment.CenterHorizontally) {
        var selectedLocale by remember { mutableStateOf(VKID.instance.internalVKIDLocale.get()) }
        DropdownSelector(
            values = mapOf(
                "system" to null,
                "en" to Locale("en"),
                "de" to Locale("de"),
                "es" to Locale("es"),
                "fr" to Locale("fr"),
                "pl" to Locale("pl"),
                "ru" to Locale("ru"),
                "tr" to Locale("tr"),
                "uk" to Locale("uk"),
            ),
            selectedValue = selectedLocale?.language ?: "system",
            onValueSelected = {
                selectedLocale = it
                VKID.instance.setLocale(it)
            },
            label = { Text("locale") },
        )
    }
}

@Composable
private fun CaptchaUtil() {
    ExpandableCard(title = "Captcha", contentAlignment = Alignment.CenterHorizontally) {
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        var redirectUri by remember { mutableStateOf("") }
        var forceError14 by remember { mutableStateOf(false) }
        var forceHitmanChallenge by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            launch(Dispatchers.IO) {
                redirectUri = CaptchaHandler.redirectUri.orEmpty()
                forceError14 = CaptchaHandler.forceError14
                forceHitmanChallenge = CaptchaHandler.forceHitmanChallenge
            }
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = redirectUri,
            onValueChange = {
                redirectUri = it
                coroutineScope.launch(Dispatchers.IO) {
                    CaptchaHandler.redirectUri = it
                    VKIDInitializer.init(context)
                }
            },
            label = { Text("Redirect uri") },
        )
        CheckboxSelector(
            title = "Error 14",
            isChecked = forceError14,
            onCheckedChange = {
                forceError14 = it
                coroutineScope.launch(Dispatchers.IO) {
                    CaptchaHandler.forceError14 = it
                    VKIDInitializer.init(context)
                }
            }
        )
        CheckboxSelector(
            title = "Hitman challenge",
            isChecked = forceHitmanChallenge,
            onCheckedChange = {
                forceHitmanChallenge = it
                coroutineScope.launch(Dispatchers.IO) {
                    CaptchaHandler.forceHitmanChallenge = it
                    VKIDInitializer.init(context)
                }
            }
        )
    }
}

private suspend fun getUserBday(context: Context, accessToken: AccessToken): String {
    return VkIdNetworkHelper.getUserBirthday(context, accessToken)
}

@Composable
private fun RevokeUtil() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Button(text = "Revoke", verticalPadding = 0.dp) {
        coroutineScope.launch {
            try {
                val accessToken = VKID.instance.accessToken
                if (accessToken != null) {
                    val success = VkIdNetworkHelper.revokeToken(context, accessToken)
                    showToast(context, if (success) "Success" else "Fail")
                } else {
                    showToast(context, "Not authorized")
                }
            } catch (@Suppress("TooGenericExceptionCaught") t: Throwable) {
                showToast(context, "Error: ${t.message}")
            }
        }
    }
}

@Composable
private fun IsFlutterUtil() {
    val context = LocalContext.current
    var isFlutter by remember { mutableStateOf<Boolean?>(null) }
    LaunchedEffect(Unit) {
        launch(Dispatchers.IO) {
            isFlutter = IsFlutterHandler.isFlutter(context)
        }
    }
    val coroutineScope = rememberCoroutineScope()
    CheckboxSelector(
        title = "Is Flutter",
        isChecked = isFlutter ?: true,
        onCheckedChange = {
            isFlutter = it
            coroutineScope.launch(Dispatchers.IO) {
                IsFlutterHandler.setIsFlutter(context, it)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Flutter flag ${if (it) "enabled" else "disabled"}. Killing app to apply changes.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                context.toActivitySafe()?.finishAffinity()
            }
        }
    )
}

@Composable
private fun StrictModeUtil() {
    val context = LocalContext.current
    var strictModeEnabled by remember { mutableStateOf<Boolean?>(null) }
    LaunchedEffect(Unit) {
        launch(Dispatchers.IO) {
            strictModeEnabled = StrictModeHandler.isStrictModeEnabled(context)
        }
    }
    val coroutineScope = rememberCoroutineScope()
    CheckboxSelector(
        title = "Strict mode enabled",
        isChecked = strictModeEnabled ?: true,
        onCheckedChange = {
            strictModeEnabled = it
            coroutineScope.launch(Dispatchers.IO) {
                StrictModeHandler.setStrictModeEnabled(context, it)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Strict mode ${if (it) "enabled" else "disabled"}. Killing app to apply changes.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                context.toActivitySafe()?.finishAffinity()
            }
        }
    )
}

private fun Context.toActivitySafe(): Activity? {
    var context = this
    while (context !is Activity && context is ContextWrapper) context = context.baseContext
    return context as? Activity
}
