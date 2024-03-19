@file:OptIn(InternalVKIDApi::class, InternalVKIDApi::class)

package com.vk.id.internal.api

import com.vk.id.common.InternalVKIDApi
import com.vk.id.internal.auth.VKIDTokenPayload
import com.vk.id.internal.auth.app.VkAuthSilentAuthProvider
import com.vk.id.network.VKIDApi
import com.vk.id.network.VKIDCall
import com.vk.id.network.wrapToVKIDCall
import okhttp3.Call
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

@Suppress("LongParameterList")
internal class VKIDApiService(
    private val api: VKIDApi,
) {

    fun getToken(
        code: String,
        codeVerifier: String,
        clientId: String,
        clientSecret: String,
        deviceId: String,
        redirectUri: String
    ): VKIDCall<VKIDTokenPayload> {
        return api.getToken(code, codeVerifier, clientId, clientSecret, deviceId, redirectUri)
            .wrapTokenToVKIDCall()
    }

    fun getSilentAuthProviders(
        clientId: String,
        clientSecret: String,
    ): VKIDCall<List<VkAuthSilentAuthProvider>> {
        return api.getSilentAuthProviders(
            clientId = clientId,
            clientSecret = clientSecret
        ).wrapToVKIDCall {
            JSONObject(requireNotNull(it.body).string())
                .getJSONArray("response")
                .parseList(VkAuthSilentAuthProvider.Companion::parse)
                ?: emptyList()
        }
    }

    private inline fun <T> JSONArray?.parseList(parser: (JSONObject) -> T) = this?.let {
        val list = ArrayList<T>(length())
        for (i in 0 until length()) optJSONObject(i)?.let { list.add(parser.invoke(it)) }
        list
    }

    @Suppress("ThrowsCount")
    private fun Call.wrapTokenToVKIDCall(): VKIDCall<VKIDTokenPayload> {
        return wrapToVKIDCall {
            if (it.body == null) throw IOException("Empty body ${it.code} $it")
            val body = requireNotNull(it.body).string()
            val jsonObject = JSONObject(body)
            if (jsonObject.has("error")) {
                throw IOException("Api error: ${it.code} $body")
            }
            try {
                VKIDTokenPayload(
                    accessToken = jsonObject.getString("access_token"),
                    userId = jsonObject.getLong("user_id"),
                    expiresIn = jsonObject.optLong("expires_in"),
                    email = jsonObject.optString("email"),
                    phone = jsonObject.optString("phone"),
                    phoneAccessKey = jsonObject.optString("phone_access_key"),
                )
            } catch (@Suppress("SwallowedException") e: JSONException) {
                val error = e.message
                throw JSONException("$error: ${it.code} $body")
            }
        }
    }
}
