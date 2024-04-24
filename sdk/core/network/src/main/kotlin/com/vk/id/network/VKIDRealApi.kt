package com.vk.id.network

import android.content.Context
import com.vk.id.common.InternalVKIDApi
import okhttp3.Call
import okhttp3.FormBody
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray

@Suppress("LongParameterList")
@InternalVKIDApi
/** Singleton **/
public class VKIDRealApi private constructor(
    private val client: OkHttpClient,
) : VKIDApi {

    override fun getToken(
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

        return createRequest(HOST_OAUTH, PATH_ACCESS_TOKEN, formBody)
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

    override fun sendStatEventsAnonymously(
        clientId: String,
        clientSecret: String,
        events: JSONArray,
    ): Call {
        val formBody = FormBody.Builder()
            .add(FIELD_API_VERSION, API_VERSION_VALUE)
            .add(FIELD_CLIENT_ID, clientId)
            .add(FIELD_CLIENT_SECRET, clientSecret)
            .add("events", events.toString())
            .build()

        return createRequest(HOST_API, "method/statEvents.addVKIDAnonymously", formBody)
    }

    private fun createRequest(host: String, path: String, requestBody: RequestBody): Call {
        val url = host.toHttpUrl().newBuilder()
            .addPathSegments(path)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        return client.newCall(request)
    }

    @InternalVKIDApi
    public companion object {
        @Volatile
        private var instance: VKIDRealApi? = null

        public fun getInstance(context: Context): VKIDRealApi {
            return instance ?: synchronized(this) {
                instance ?: VKIDRealApi(OkHttpClientProvider(context).provide()).also { instance = it }
            }
        }

        private const val HOST_API = "https://api.vk.com"
        private const val HOST_OAUTH = "https://oauth.vk.com"

        private const val PATH_SILENT_AUTH_PROVIDERS = "method/auth.getSilentAuthProviders"
        private const val PATH_ACCESS_TOKEN = "access_token"

        private const val FIELD_CLIENT_ID = "client_id"
        private const val FIELD_CLIENT_SECRET = "client_secret"
        private const val FIELD_CODE = "code"
        private const val FIELD_CODE_VERIFIER = "code_verifier"
        private const val FIELD_DEVICE_ID = "device_id"
        private const val FIELD_REDIRECT_URI = "redirect_uri"

        private const val FIELD_API_VERSION = "v"
        private const val API_VERSION_VALUE = "5.220"
    }
}
