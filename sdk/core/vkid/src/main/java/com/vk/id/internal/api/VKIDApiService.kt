package com.vk.id.internal.api

import com.vk.id.VKIDInvalidTokenException
import com.vk.id.internal.api.dto.VKIDUserInfoPayload
import com.vk.id.internal.auth.VKIDTokenPayload
import com.vk.id.internal.auth.app.VkAuthSilentAuthProvider
import okhttp3.Call
import okhttp3.Response
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
        deviceId: String,
        redirectUri: String,
        state: String,
    ): VKIDCall<VKIDTokenPayload> {
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
    ): VKIDCall<VKIDUserInfoPayload> {
        val call = api.getUser(
            accessToken = accessToken,
            clientId = clientId,
            deviceId = deviceId,
        )
        return object : VKIDCall<VKIDUserInfoPayload> {
            override fun execute(): Result<VKIDUserInfoPayload> {
                val result = call.execute()
                val body = JSONObject(requireNotNull(result.body).string())
                return when {
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

            override fun cancel() {
                call.cancel()
            }
        }
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

    fun refreshToken(
        refreshToken: String,
        clientId: String,
        deviceId: String,
        state: String,
    ): VKIDCall<VKIDTokenPayload> {
        return api.refreshToken(
            refreshToken = refreshToken,
            clientId = clientId,
            deviceId = deviceId,
            state = state,
        ).wrapTokenToVKIDCall()
    }

    fun exchangeToken(
        v1Token: String,
        clientId: String,
        deviceId: String,
        state: String,
    ): VKIDCall<VKIDTokenPayload> {
        return api.exchangeToken(
            v1Token = v1Token,
            clientId = clientId,
            deviceId = deviceId,
            state = state,
        ).wrapTokenToVKIDCall()
    }

    fun logout(
        accessToken: String,
        clientId: String,
        deviceId: String,
    ): VKIDCall<Unit> {
        val call = api.logout(
            accessToken = accessToken,
            clientId = clientId,
            deviceId = deviceId,
        )
        return object : VKIDCall<Unit> {
            override fun execute(): Result<Unit> {
                val result = call.execute()
                val body = JSONObject(requireNotNull(result.body).string())
                return when {
                    body.isNull("error") -> Result.success(Unit)
                    body.getString("error") == "invalid_token" -> Result.failure(VKIDInvalidTokenException())
                    else -> Result.failure(IOException())
                }
            }

            override fun cancel() {
                call.cancel()
            }
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
                    refreshToken = jsonObject.getString("refresh_token"),
                    idToken = jsonObject.optString("id_token"),
                    userId = jsonObject.getLong("user_id"),
                    expiresIn = jsonObject.optLong("expires_in"),
                    state = jsonObject.optString("state"),
                )
            } catch (@Suppress("SwallowedException") e: JSONException) {
                val error = e.message
                throw JSONException("$error: ${it.code} $body")
            }
        }
    }

    private fun <T> Call.wrapToVKIDCall(
        responseMapping: (response: Response) -> T,
    ): VKIDCall<T> {
        return object : VKIDCall<T> {
            override fun execute(): Result<T> {
                return try {
                    val response = this@wrapToVKIDCall.execute()
                    Result.success(responseMapping(response))
                } catch (ioe: IOException) {
                    Result.failure(ioe)
                } catch (je: JSONException) {
                    Result.failure(je)
                }
            }

            override fun cancel() = this@wrapToVKIDCall.cancel()
        }
    }
}
