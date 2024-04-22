package com.vk.id.internal.api

import okhttp3.Call
import okhttp3.FormBody
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

@Suppress("LongParameterList")
internal class VKIDRealApi(
    private val client: OkHttpClient,
) : VKIDApi {

    override fun getToken(
        code: String,
        codeVerifier: String,
        clientId: String,
        deviceId: String,
        redirectUri: String,
        state: String,
    ): Call {
        val formBody = FormBody.Builder()
            .add(FIELD_GRANT_TYPE, VALUE_AUTHORIZATION_CODE)
            .add(FIELD_CODE, code)
            .add(FIELD_CODE_VERIFIER, codeVerifier)
            .add(FIELD_CLIENT_ID, clientId)
            .add(FIELD_DEVICE_ID, deviceId)
            .add(FIELD_REDIRECT_URI, redirectUri)
            .add(FIELD_STATE, state)
            .build()

        return createRequest(HOST_VK_ID, PATH_AUTH, formBody)
    }

    override fun getSilentAuthProviders(
        clientId: String,
        clientSecret: String
    ): Call {
        val formBody = FormBody.Builder()
            .add(FIELD_API_VERSION, API_VERSION_VALUE)
            .add(FIELD_CLIENT_ID, clientId)
            .add(FIELD_CLIENT_SECRET, clientSecret)
            .build()

        return createRequest(HOST_API, PATH_SILENT_AUTH_PROVIDERS, formBody)
    }

    override fun refreshToken(
        refreshToken: String,
        clientId: String,
        deviceId: String,
        state: String
    ): Call {
        val formBody = FormBody.Builder()
            .add(FIELD_GRANT_TYPE, VALUE_REFRESH_TOKEN)
            .add(FIELD_REFRESH_TOKEN, refreshToken)
            .add(FIELD_CLIENT_ID, clientId)
            .add(FIELD_DEVICE_ID, deviceId)
            .add(FIELD_STATE, state)
            .build()

        return createRequest(HOST_VK_ID, PATH_AUTH, formBody)
    }

    override fun getUser(
        accessToken: String,
        clientId: String,
        deviceId: String,
    ): Call {
        val formBody = FormBody.Builder()
            .add(FIELD_ACCESS_TOKEN, accessToken)
            .add(FIELD_DEVICE_ID, deviceId)
            .build()

        return createRequest(HOST_VK_ID, PATH_USER_INFO, formBody, query = mapOf(FIELD_CLIENT_ID to clientId))
    }

    override fun exchangeToken(
        v1Token: String,
        clientId: String,
        deviceId: String,
        state: String
    ): Call {
        val formBody = FormBody.Builder()
            .add(FIELD_RESPONSE_TYPE, VALUE_TOKEN)
            .add(FIELD_GRANT_TYPE, VALUE_ACCESS_TOKEN)
            .add(FIELD_ACCESS_TOKEN, v1Token)
            .add(FIELD_CLIENT_ID, clientId)
            .add(FIELD_DEVICE_ID, deviceId)
            .add(FIELD_STATE, state)
            .build()

        return createRequest(HOST_VK_ID, PATH_USER_INFO, formBody)
    }

    override fun logout(
        accessToken: String,
        clientId: String,
        deviceId: String
    ): Call {
        val formBody = FormBody.Builder()
            .add(FIELD_ACCESS_TOKEN, accessToken)
            .add(FIELD_CLIENT_ID, clientId)
            .add(FIELD_DEVICE_ID, deviceId)
            .build()

        return createRequest(HOST_VK_ID, PATH_LOGOUT, formBody)
    }

    private fun createRequest(
        host: String,
        path: String,
        requestBody: RequestBody,
        query: Map<String, String> = emptyMap(),
    ): Call {
        val url = host.toHttpUrl().newBuilder()
            .apply {
                query.forEach { (name, value) ->
                    addQueryParameter(name, value)
                }
            }
            .addPathSegments(path)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        return client.newCall(request)
    }

    companion object {
        private const val HOST_API = "https://api.vk.com"

        // todo: Change to actual host after oauth2 completion
        private const val HOST_VK_ID = "https://tk-training.id.cs7777.vk.com"

        private const val PATH_SILENT_AUTH_PROVIDERS = "method/auth.getSilentAuthProviders"
        private const val PATH_AUTH = "oauth2/auth"
        private const val PATH_USER_INFO = "oauth2/user_info"
        private const val PATH_LOGOUT = "oauth2/logout"

        private const val FIELD_CLIENT_ID = "client_id"
        private const val FIELD_CLIENT_SECRET = "client_secret"
        private const val FIELD_CODE = "code"
        private const val FIELD_CODE_VERIFIER = "code_verifier"
        private const val FIELD_DEVICE_ID = "device_id"
        private const val FIELD_REDIRECT_URI = "redirect_uri"
        private const val FIELD_GRANT_TYPE = "grant_type"
        private const val FIELD_STATE = "state"
        private const val FIELD_REFRESH_TOKEN = "refresh_token"
        private const val FIELD_ACCESS_TOKEN = "access_token"
        private const val FIELD_RESPONSE_TYPE = "response_type"

        private const val FIELD_API_VERSION = "v"
        private const val API_VERSION_VALUE = "5.220"
        private const val VALUE_AUTHORIZATION_CODE = "authorization_code"
        private const val VALUE_REFRESH_TOKEN = "refresh_token"
        private const val VALUE_TOKEN = "token"
        private const val VALUE_ACCESS_TOKEN = "access_token"
    }
}
