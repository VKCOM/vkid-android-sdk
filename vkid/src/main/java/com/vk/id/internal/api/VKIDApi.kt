package com.vk.id.internal.api

import okhttp3.Call
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okio.ByteString.Companion.encodeUtf8

internal class VKIDApi(
    private val client: OkHttpClient,
    private val host: HttpUrl = HOST.toHttpUrl()
) {

    fun getToken(
        code: String,
        codeVerifier: String,
        clientId: String,
        clientSecret: String,
        deviceId: String,
        redirectUri: String
    ): Call {
        val formBody = FormBody.Builder()
            .add(FIELD_CODE, code)
            .add(FIELD_CODE_VERIFIER, codeVerifier)
            .add(FIELD_CLIENT_ID, clientId)
            .add(FIELD_CLIENT_SECRET, clientSecret)
            .add(FIELD_DEVICE_ID, deviceId)
            .add(FIELD_REDIRECT_URI, redirectUri)
            .build()

        return createRequest(PATH_ACCESS_TOKEN, formBody, clientId)
    }

    fun refreshToken(refreshToken: String, clientId: String): Call {
        TODO()
    }

    fun revokeToken(token: String, tokenHint: String, clientId: String): Call {
        TODO()
    }

    private fun createRequest(path: String, requestBody: RequestBody, clientId: String): Call {
        val url = host.newBuilder()
            .addPathSegments(path)
            .build()
        val request = Request.Builder()
            //.addHeader(HEADER_AUTHORIZATION, generateBasicHeader(clientId))
            //.addHeader(HEADER_ACCEPT, "application/json")
            //.addHeader(HEADER_X_SSO_NO_ADAPTER, "true")
            .url(url)
            .post(requestBody)
            .build()
        return client.newCall(request)
    }

    private fun generateBasicHeader(clientId: String): String {
        val encodedClientId = "$clientId:".encodeUtf8().base64()
        return "Basic $encodedClientId"
    }

    companion object {
        private const val HOST = "https://oauth.vk.com"
        private const val PATH_ACCESS_TOKEN = "access_token"

        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val HEADER_ACCEPT = "Accept"
        private const val HEADER_X_SSO_NO_ADAPTER = "X-SSO-No-Adapter"

        private const val FIELD_CLIENT_ID = "client_id"
        private const val FIELD_CLIENT_SECRET = "client_secret"
        private const val FIELD_CODE = "code"
        private const val FIELD_CODE_VERIFIER = "code_verifier"
        private const val FIELD_DEVICE_ID = "device_id"
        private const val FIELD_REDIRECT_URI = "redirect_uri"
    }
}
