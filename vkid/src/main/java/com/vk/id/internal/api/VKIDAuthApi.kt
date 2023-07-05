/*
 * Copyright © 2021 Tinkoff Bank
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vk.id.internal.api

import okhttp3.Call
import okhttp3.FormBody
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

internal class VKIDAuthApi(
    private val client: OkHttpClient
) {

    fun getSilentAuthProviders(
        clientId: String,
        clientSecret: String
    ): Call {
        val formBody = FormBody.Builder()
            .add(FIELD_API_VERSION, API_VERSION_VALUE)
            .add(FIELD_CLIENT_ID, clientId)
            .add(FIELD_CLIENT_SECRET, clientSecret)
            .build()

        return createRequest(PATH_SILENT_AUTH_PROVIDERS, formBody)
    }

    private fun createRequest(path: String, requestBody: RequestBody): Call {
        val url = HOST.toHttpUrl().newBuilder()
            .addPathSegments(path)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        return client.newCall(request)
    }

    companion object {
        private const val HOST = "https://api.vk.com/method"
        private const val PATH_SILENT_AUTH_PROVIDERS = "auth.getSilentAuthProviders"

        private const val FIELD_CLIENT_ID = "client_id"
        private const val FIELD_CLIENT_SECRET = "client_secret"
        private const val FIELD_API_VERSION = "v"

        private const val API_VERSION_VALUE = "5.220"
    }
}
