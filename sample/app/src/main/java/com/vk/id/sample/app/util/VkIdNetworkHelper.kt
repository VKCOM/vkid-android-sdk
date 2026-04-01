@file:OptIn(InternalVKIDApi::class)

package com.vk.id.sample.app.util

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.ComponentInfoFlags
import android.os.Build
import com.vk.id.AccessToken
import com.vk.id.VKIDUser
import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.HttpClientProvider
import com.vk.id.network.http.FormBody
import com.vk.id.network.http.HttpRequest
import com.vk.id.network.http.HttpResponse
import com.vk.id.sample.app.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.UUID

internal object VkIdNetworkHelper {

    @Suppress("MagicNumber")
    suspend fun exchangeAuthCode(
        context: Context,
        code: String,
        codeVerifier: String,
        deviceId: String
    ): AccessToken = withContext(Dispatchers.IO) {
        val endpoint = "https://id.vk.ru"
        val clientId = getClientId(context)
        val redirectUri = getRedirectUri(context)

        val formBody = FormBody.Builder().apply {
            add("grant_type", "authorization_code")
            add("code", code)
            add("code_verifier", codeVerifier)
            add("client_id", clientId)
            add("device_id", deviceId)
            add("redirect_uri", redirectUri)
            add("state", UUID.randomUUID().toString())
        }.build()

        val path = "oauth2/auth"

        val request = createRequest(endpoint, path, formBody)

        val httpClient = HttpClientProvider(context).provide()
        val result: Result<HttpResponse> = httpClient.executeRequest(request)

        val response = result.getOrThrow()
        val jsonObject = JSONObject(response.body)

        AccessToken(
            token = jsonObject.getString("access_token"),
            idToken = jsonObject.optString("id_token"),
            userID = jsonObject.getLong("user_id"),
            expireTime = jsonObject.optLong("expires_in").let {
                if (it > 0) {
                    System.currentTimeMillis() + it * 1000
                } else {
                    -1
                }
            },
            userData = VKIDUser(
                firstName = "User should be fetched by token",
                lastName = "",
            ),
            scopes = jsonObject.getString("scope").split(' ').toSet(),
        )
    }

    suspend fun getPublicInfo(
        context: Context,
        idToken: String
    ): VKIDUser? = withContext(Dispatchers.IO) {
        val endpoint = "https://id.vk.ru"

        val formBody = FormBody.Builder().apply {
            add("id_token", idToken)
            add("client_id", getClientId(context))
        }.build()

        val request = createRequest(endpoint, "oauth2/public_info", formBody)

        val httpClient = HttpClientProvider(context).provide()
        val result = httpClient.executeRequest(request)

        val response = result.getOrThrow()
        val jsonObject = JSONObject(response.body)
        val user = jsonObject.getJSONObject("user")

        VKIDUser(
            firstName = user.optString("first_name"),
            lastName = user.optString("last_name"),
            phone = user.optString("phone"),
            photo200 = user.optString("avatar"),
            email = user.optString("email"),
        )
    }

    suspend fun revokeToken(
        context: Context,
        accessToken: AccessToken
    ): Boolean = withContext(Dispatchers.IO) {
        val endpoint = "https://id.vk.ru"

        val formBody = FormBody.Builder().apply {
            add("access_token", accessToken.token)
            add("client_id", getClientId(context))
        }.build()

        val request = createRequest(endpoint, "oauth2/revoke", formBody)

        val httpClient = HttpClientProvider(context).provide()
        val result = httpClient.executeRequest(request)

        val response = result.getOrThrow()
        val jsonObject = JSONObject(response.body)
        val code = jsonObject.optInt("response")
        code == 1
    }

    suspend fun getUserBirthday(context: Context, accessToken: AccessToken): String = withContext(Dispatchers.IO) {
        val url = "https://api.vk.ru/method/users.get?user_ids=${accessToken.userID}" +
            "&fields=bdate&access_token=${accessToken.token}&v=5.131"

        val request = HttpRequest.get(url)
        val httpClient = HttpClientProvider(context).provide()
        val result = httpClient.executeRequest(request)

        val response = result.getOrThrow()
        val responseJson = JSONObject(response.body)
            .getJSONArray("response")
            .getJSONObject(0)
        responseJson.getString("bdate")
    }

    private fun getClientId(context: Context): String {
        val ai = getActivityInfo(context)
        return ai.metaData.getInt("VKIDClientID").toString()
    }

    private fun getRedirectUri(context: Context): String {
        val ai = getActivityInfo(context)
        val redirectScheme = ai.metaData.getString("VKIDRedirectScheme") ?: error("No VKIDRedirectScheme in metadata")
        val redirectHost = ai.metaData.getString("VKIDRedirectHost") ?: error("No VKIDRedirectHost in metadata")
        return "$redirectScheme://$redirectHost/blank.html"
    }

    @SuppressLint("WrongConstant")
    private fun getActivityInfo(context: Context): ActivityInfo {
        val componentName = ComponentName(context, MainActivity::class.java)
        val flags = PackageManager.GET_META_DATA or PackageManager.GET_ACTIVITIES
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getActivityInfo(componentName, ComponentInfoFlags.of(flags.toLong()))
        } else {
            context.packageManager.getActivityInfo(componentName, flags)
        }
    }

    private fun createRequest(
        endpoint: String,
        path: String,
        formBody: FormBody
    ): HttpRequest = HttpRequest.post(url = "$endpoint/$path", body = formBody)
}
