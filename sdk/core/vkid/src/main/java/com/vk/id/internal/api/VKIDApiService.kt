@file:OptIn(InternalVKIDApi::class, InternalVKIDApi::class)

package com.vk.id.internal.api

import com.vk.id.VKIDInvalidTokenException
import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.api.dto.VKIDUserInfoPayload
import com.vk.id.internal.auth.VKIDCodePayload
import com.vk.id.internal.auth.VKIDTokenPayload
import com.vk.id.internal.auth.app.VkAuthSilentAuthProvider
import com.vk.id.network.InternalVKIDApiContract
import com.vk.id.network.InternalVKIDCall
import com.vk.id.network.internalVKIDWrapToVKIDCall
import okhttp3.Call
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

@Suppress("LongParameterList")
internal class VKIDApiService(
    private val api: InternalVKIDApiContract,
) {

    fun getToken(
        code: String,
        codeVerifier: String,
        clientId: String,
        deviceId: String,
        redirectUri: String,
        state: String,
    ): InternalVKIDCall<VKIDTokenPayload> {
        return api.getToken(
            code = code,
            codeVerifier = codeVerifier,
            clientId = clientId,
            deviceId = deviceId,
            redirectUri = redirectUri,
            state = state,
        ).wrapTokenToVKIDCall()
    }

    fun getUserInfo(
        accessToken: String,
        clientId: String,
        deviceId: String,
    ): InternalVKIDCall<VKIDUserInfoPayload> {
        return api.getUser(
            accessToken = accessToken,
            clientId = clientId,
            deviceId = deviceId,
        ).internalVKIDWrapToVKIDCall {
            val body = JSONObject(requireNotNull(it.body).string())
            when {
                body.isNull("error") -> {
                    val user = body.getJSONObject("user")
                    Result.success(
                        VKIDUserInfoPayload(
                            firstName = user.optString("first_name"),
                            lastName = user.optString("last_name"),
                            phone = user.optString("phone"),
                            avatar = user.optString("avatar"),
                            email = user.optString("email"),
                        )
                    )
                }
                body.getString("error") == "invalid_token" -> Result.failure(VKIDInvalidTokenException())
                else -> Result.failure(IOException())
            }
        }
    }

    fun getSilentAuthProviders(
        clientId: String,
        clientSecret: String,
    ): InternalVKIDCall<List<VkAuthSilentAuthProvider>> {
        return api.getSilentAuthProviders(
            clientId = clientId,
            clientSecret = clientSecret
        ).internalVKIDWrapToVKIDCall {
            Result.success(
                JSONObject(requireNotNull(it.body).string())
                    .getJSONArray("response")
                    .parseList(VkAuthSilentAuthProvider.Companion::parse)
                    ?: emptyList()
            )
        }
    }

    fun refreshToken(
        refreshToken: String,
        clientId: String,
        deviceId: String,
        state: String,
    ): InternalVKIDCall<VKIDTokenPayload> {
        return api.refreshToken(
            refreshToken = refreshToken,
            clientId = clientId,
            deviceId = deviceId,
            state = state,
        ).wrapTokenToVKIDCall()
    }

    @Suppress("ThrowsCount")
    fun exchangeToken(
        v1Token: String,
        clientId: String,
        deviceId: String,
        state: String,
        codeChallenge: String,
    ): InternalVKIDCall<VKIDCodePayload> {
        return api.exchangeToken(
            v1Token = v1Token,
            clientId = clientId,
            deviceId = deviceId,
            state = state,
            codeChallenge = codeChallenge,
        ).internalVKIDWrapToVKIDCall {
            if (it.body == null) throw IOException("Empty body ${it.code} $it")
            val body = requireNotNull(it.body).string()
            val jsonObject = JSONObject(body)
            if (jsonObject.has("error")) {
                throw IOException("Api error: ${it.code} $body")
            }
            try {
                Result.success(
                    VKIDCodePayload(
                        code = jsonObject.getString("code"),
                        state = jsonObject.optString("state"),
                        deviceId = jsonObject.optString("device_id"),
                    )
                )
            } catch (@Suppress("SwallowedException") e: JSONException) {
                val error = e.message
                throw JSONException("$error: ${it.code} $body")
            }
        }
    }

    fun logout(
        accessToken: String,
        clientId: String,
        deviceId: String,
    ): InternalVKIDCall<Unit> {
        return api.logout(
            accessToken = accessToken,
            clientId = clientId,
            deviceId = deviceId,
        ).internalVKIDWrapToVKIDCall {
            val body = JSONObject(requireNotNull(it.body).string())
            when {
                body.isNull("error") -> Result.success(Unit)
                body.getString("error") == "invalid_token" -> Result.failure(VKIDInvalidTokenException())
                else -> Result.failure(IOException())
            }
        }
    }

    private inline fun <T> JSONArray?.parseList(parser: (JSONObject) -> T) = this?.let {
        val list = ArrayList<T>(length())
        for (i in 0 until length()) optJSONObject(i)?.let { list.add(parser.invoke(it)) }
        list
    }

    @Suppress("ThrowsCount")
    private fun Call.wrapTokenToVKIDCall(): InternalVKIDCall<VKIDTokenPayload> {
        return internalVKIDWrapToVKIDCall {
            if (it.body == null) throw IOException("Empty body ${it.code} $it")
            val body = requireNotNull(it.body).string()
            val jsonObject = JSONObject(body)
            if (jsonObject.has("error")) {
                throw IOException("Api error: ${it.code} $body")
            }
            try {
                Result.success(
                    VKIDTokenPayload(
                        accessToken = jsonObject.getString("access_token"),
                        refreshToken = jsonObject.getString("refresh_token"),
                        idToken = jsonObject.optString("id_token"),
                        userId = jsonObject.getLong("user_id"),
                        expiresIn = jsonObject.optLong("expires_in"),
                        state = jsonObject.optString("state"),
                    )
                )
            } catch (@Suppress("SwallowedException") e: JSONException) {
                val error = e.message
                throw JSONException("$error: ${it.code} $body")
            }
        }
    }
}
