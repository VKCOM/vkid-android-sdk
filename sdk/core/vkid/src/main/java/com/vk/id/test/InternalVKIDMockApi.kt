package com.vk.id.test

import androidx.annotation.WorkerThread
import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.InternalVKIDApiContract
import com.vk.id.network.InternalVKIDCall
import com.vk.id.network.http.HttpRequest
import com.vk.id.network.http.HttpResponse
import org.json.JSONArray

@InternalVKIDApi
internal class InternalVKIDMockApi(
    private val testBuilder: InternalVKIDTestBuilder
) : InternalVKIDApiContract {

    @Suppress("LongParameterList")
    override fun getToken(
        code: String,
        codeVerifier: String,
        clientId: String,
        deviceId: String,
        redirectUri: String,
        state: String,
    ): InternalVKIDCall<HttpResponse> {
        val response = testBuilder.getTokenResponseValue
        return MockInternalVKIDCall(
            createSuccessTokenResponse(response)
        )
    }

    override fun getSilentAuthProviders(
        clientId: String,
        clientSecret: String,
    ): InternalVKIDCall<HttpResponse> {
        val silentAuthResponse = testBuilder.getSilentAuthProvidersResponseValue
        return if (silentAuthResponse.response.isNotEmpty()) {
            MockInternalVKIDCall(
                createSuccessSilentAuthProvidersResponse(silentAuthResponse.response)
            )
        } else {
            MockInternalVKIDCall(
                createErrorResponse("not mocked")
            )
        }
    }

    override fun refreshToken(
        refreshToken: String,
        clientId: String,
        deviceId: String,
        state: String,
    ): InternalVKIDCall<HttpResponse> {
        val response = testBuilder.refreshTokenResponseValue
        return MockInternalVKIDCall(
            createSuccessTokenResponse(response)
        )
    }

    override fun getUser(
        accessToken: String,
        clientId: String,
        deviceId: String,
    ): InternalVKIDCall<HttpResponse> {
        val response = testBuilder.getUserInfoResponsesValue
        val userInfo = response?.getOrNull()
        return if (userInfo == null) {
            MockInternalVKIDCall(
                createErrorResponse(response?.exceptionOrNull()?.message ?: "user error")
            )
        } else if (userInfo.error != null) {
            MockInternalVKIDCall(
                createErrorResponse(userInfo.error!!)
            )
        } else if (userInfo.user != null) {
            MockInternalVKIDCall(
                createSuccessUserInfoResponse(userInfo)
            )
        } else {
            MockInternalVKIDCall(
                createErrorResponse("not mocked")
            )
        }
    }

    @Suppress("LongParameterList")
    override fun exchangeToken(
        v1Token: String,
        clientId: String,
        deviceId: String,
        state: String,
        codeChallenge: String,
    ): InternalVKIDCall<HttpResponse> {
        val response = testBuilder.exchangeTokenResponseValue
        return MockInternalVKIDCall(
            createSuccessCodeResponse(response)
        )
    }

    override fun logout(
        accessToken: String,
        clientId: String,
        deviceId: String,
    ): InternalVKIDCall<HttpResponse> {
        val response = testBuilder.logoutResponseValue
        return MockInternalVKIDCall(
            createSuccessLogoutResponse(response)
        )
    }

    @Suppress("LongParameterList")
    override fun sendStatEventsAnonymously(
        clientId: String,
        clientSecret: String,
        sakVersion: String,
        events: JSONArray,
        externalDeviceId: String,
    ): InternalVKIDCall<HttpResponse> {
        return MockInternalVKIDCall(
            createSuccessStatResponse()
        )
    }

    @Suppress("LongParameterList")
    override fun sendStatEvents(
        accessToken: String,
        clientId: String,
        clientSecret: String,
        sakVersion: String,
        events: JSONArray,
        externalDeviceId: String,
    ): InternalVKIDCall<HttpResponse> {
        return MockInternalVKIDCall(
            createSuccessStatResponse()
        )
    }

    @InternalVKIDApi
    private fun createSuccessTokenResponse(
        response: InternalVKIDTokenPayloadResponse
    ): Result<HttpResponse> {
        val json = """
            {
                "access_token": "${response.accessToken ?: "mock"}",
                "refresh_token": "${response.refreshToken ?: "mock"}",
                "id_token": "${response.idToken ?: "mock"}",
                "expires_in": ${response.expiresIn ?: 1000},
                "user_id": ${response.userId ?: 0},
                "state": "${response.state ?: "state"}",
                "scope": "${response.scope ?: "phone email"}"
            }
        """.trimIndent()
        return createHttpResponse(json)
    }

    @InternalVKIDApi
    private fun createSuccessCodeResponse(
        response: InternalVKIDCodePayloadResponse
    ): Result<HttpResponse> {
        val json = if (response.error != null) {
            """{"error":"${response.error}"}"""
        } else {
            """
            {
                "code": "${response.code ?: "mock_code"}",
                "state": "${response.state ?: "state"}",
                "device_id": "${response.deviceId ?: "mock_device_id"}"
            }
            """.trimIndent()
        }
        return createHttpResponse(json)
    }

    @InternalVKIDApi
    private fun createSuccessUserInfoResponse(
        response: InternalVKIDUserInfoPayloadResponse
    ): Result<HttpResponse> {
        val user = response.user ?: return createErrorResponse("user is null")
        val json = """
            {
                "user": {
                    "first_name": "${user.firstName}",
                    "last_name": "${user.lastName}",
                    "phone": "${user.phone}",
                    "email": "${user.email}",
                    "avatar": "${user.avatar}"
                },
                "state": "${response.state ?: "state"}"
            }
        """.trimIndent()
        return createHttpResponse(json)
    }

    @InternalVKIDApi
    private fun createSuccessSilentAuthProvidersResponse(
        providers: List<String>
    ): Result<HttpResponse> {
        val providersJson = providers.joinToString(", ") { "\"$it\"" }
        val json = """
            {
                "response": [$providersJson]
            }
        """.trimIndent()
        return createHttpResponse(json)
    }

    @InternalVKIDApi
    private fun createSuccessLogoutResponse(response: InternalVKIDLogoutPayloadResponse): Result<HttpResponse> {
        return createHttpResponse(
            if (response.error != null) { """{"error":"${response.error}"}""" } else "{}"
        )
    }

    @InternalVKIDApi
    private fun createSuccessStatResponse(): Result<HttpResponse> {
        return createHttpResponse("""{"response": 1}""")
    }

    @InternalVKIDApi
    private fun createErrorResponse(
        error: String,
        code: Int = 400
    ): Result<HttpResponse> {
        val json = """{"error": "$error"}"""
        return createHttpResponse(json, code)
    }

    @WorkerThread
    @InternalVKIDApi
    private fun createHttpResponse(
        json: String,
        code: Int = 200
    ): Result<HttpResponse> {
        val mockRequest = HttpRequest(
            url = "https://mock.url",
            method = "GET",
            headers = emptyMap(),
            body = ""
        )
        val response = com.vk.id.network.http.HttpResponse(
            request = mockRequest,
            code = code,
            message = null,
            body = json,
            headers = mapOf("content-type" to "application/json"),
            isRequestSuccessful = true
        )
        return Result.success(response)
    }
}
