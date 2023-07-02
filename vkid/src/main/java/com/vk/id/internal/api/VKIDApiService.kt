package com.vk.id.internal.api

import com.vk.id.VKIDCall
import com.vk.id.internal.auth.VKIDTokenPayload
import okhttp3.Call
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

internal class VKIDApiService(private val api: VKIDApi) {

    fun getToken(
        code: String,
        codeVerifier: String,
        clientId: String,
        clientSecret: String,
        deviceId: String,
        redirectUri: String
    ): VKIDCall<VKIDTokenPayload> {
        return api.getToken(code, codeVerifier, clientId, clientSecret, deviceId, redirectUri).wrapTokenToVKIDCall()
    }

    private fun Call.wrapTokenToVKIDCall(): VKIDCall<VKIDTokenPayload> {
        return wrapToVKIDCall {
            if (it.body == null) throw IOException("Empty body $it")
            val jsonObject = JSONObject(requireNotNull(it.body).string())
            VKIDTokenPayload(
                accessToken = jsonObject.getString("access_token"),
                userId = jsonObject.getLong("user_id"),
                expiresIn = jsonObject.optLong("expires_in"),
                email =  jsonObject.optString("email"),
                phone = jsonObject.optString("phone"),
                phoneAccessKey = jsonObject.optString("phone_access_key"),
            )
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
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }

            override fun cancel() = this@wrapToVKIDCall.cancel()
        }
    }
}
