@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http

import android.content.SharedPreferences
import com.vk.id.common.InternalVKIDApi
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.IOException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

internal class ApiDomainFallbackInterceptorTest : BehaviorSpec({
    Given("a request to api.vk.ru") {
        When("the primary endpoint responds successfully") {
            val interceptor = ApiDomainFallbackInterceptor()
            val chain = RecordingChain { request -> response(request, 200) }

            val result = interceptor.intercept(chain)

            Then("it returns the primary response without another request") {
                result.code shouldBe 200
                chain.requests.map(HttpRequest::url) shouldBe listOf(PRIMARY_URL)
            }
        }

        When("an idempotent primary request fails with an ordinary I/O error") {
            val interceptor = ApiDomainFallbackInterceptor()
            val chain = RecordingChain(
                initialRequest = HttpRequest.get(PRIMARY_URL),
            ) { request ->
                if (request.url.startsWith(PRIMARY_ENDPOINT)) {
                    throw IOException("connection refused")
                }
                response(request, 200)
            }

            Then("it returns the error without fallback") {
                shouldThrow<IOException> { interceptor.intercept(chain) }
                chain.requests.map(HttpRequest::url) shouldBe listOf(PRIMARY_URL)
            }
        }

        When("a non-idempotent primary request fails with an I/O error") {
            val interceptor = ApiDomainFallbackInterceptor()
            val chain = RecordingChain { throw IOException("connection refused") }

            Then("it returns the error without fallback") {
                shouldThrow<IOException> { interceptor.intercept(chain) }
                chain.requests.map(HttpRequest::url) shouldBe listOf(PRIMARY_URL)
            }
        }

        When("a non-idempotent primary request fails because of SSL") {
            val interceptor = ApiDomainFallbackInterceptor()
            val chain = RecordingChain { request ->
                if (request.url.startsWith(PRIMARY_ENDPOINT)) {
                    throw IOException("request failed", SSLException("handshake failed"))
                }
                response(request, 200)
            }

            val result = interceptor.intercept(chain)

            Then("it immediately retries the request at api.vk.ru") {
                result.code shouldBe 200
                chain.requests.map(HttpRequest::url) shouldBe listOf(PRIMARY_URL, FALLBACK_URL)
            }
        }

        When("a non-idempotent primary request fails because DNS cannot resolve its host") {
            val interceptor = ApiDomainFallbackInterceptor()
            val chain = RecordingChain { request ->
                if (request.url.startsWith(PRIMARY_ENDPOINT)) {
                    throw IOException("request failed", UnknownHostException("partners.api.vk.ru"))
                }
                response(request, 200)
            }

            val result = interceptor.intercept(chain)

            Then("it immediately retries the request at api.vk.ru") {
                result.code shouldBe 200
                chain.requests.map(HttpRequest::url) shouldBe listOf(PRIMARY_URL, FALLBACK_URL)
            }
        }

        When("the HTTPS proxy returns 503 while opening a CONNECT tunnel") {
            val interceptor = ApiDomainFallbackInterceptor()
            val chain = RecordingChain { request ->
                if (request.url.startsWith(PRIMARY_ENDPOINT)) {
                    throw IOException(
                        "Network error: Failed to send request body",
                        IOException(
                            "Failed to send request body",
                            IOException("Unexpected response code for CONNECT: 503"),
                        ),
                    )
                }
                response(request, 200)
            }

            val result = interceptor.intercept(chain)

            Then("it retries the request at api.vk.ru") {
                result.code shouldBe 200
                chain.requests.map(HttpRequest::url) shouldBe listOf(PRIMARY_URL, FALLBACK_URL)
            }
        }

        When("the active fallback host later fails") {
            val interceptor = ApiDomainFallbackInterceptor()
            val initialChain = RecordingChain(
                initialRequest = HttpRequest.get(PRIMARY_URL),
            ) { request ->
                if (request.url.startsWith(PRIMARY_ENDPOINT)) {
                    throw UnknownHostException("internal-sdk.api.vk.ru")
                }
                response(request, 200)
            }
            interceptor.intercept(initialChain)

            val nextChain = RecordingChain(
                initialRequest = HttpRequest.get(PRIMARY_URL),
            ) { request ->
                if (request.url.startsWith(FALLBACK_ENDPOINT)) {
                    throw UnknownHostException("api.vk.ru")
                }
                response(request, 200)
            }

            val result = interceptor.intercept(nextChain)

            Then("it starts from the remembered fallback host and advances to the next reserve host") {
                result.code shouldBe 200
                nextChain.requests.map(HttpRequest::url) shouldBe listOf(FALLBACK_URL, SECONDARY_FALLBACK_URL)
            }
        }

        When("a non-idempotent primary request responds with a server error") {
            val interceptor = ApiDomainFallbackInterceptor()
            val chain = RecordingChain { request ->
                if (request.url.startsWith(PRIMARY_ENDPOINT)) {
                    response(request, 503)
                } else {
                    response(request, 200)
                }
            }

            val result = interceptor.intercept(chain)

            Then("it returns the primary response without fallback") {
                result.code shouldBe 503
                chain.requests.map(HttpRequest::url) shouldBe listOf(PRIMARY_URL)
            }
        }

        When("an idempotent primary request responds with a server error") {
            val interceptor = ApiDomainFallbackInterceptor()
            val chain = RecordingChain(
                initialRequest = HttpRequest.get(PRIMARY_URL),
            ) { request ->
                if (request.url.startsWith(PRIMARY_ENDPOINT)) {
                    response(request, 503)
                } else {
                    response(request, 200)
                }
            }

            Then("it returns the primary response without fallback") {
                val result = interceptor.intercept(chain)

                result.code shouldBe 503
                chain.requests.map(HttpRequest::url) shouldBe listOf(PRIMARY_URL)
            }
        }

        When("the primary endpoint responds with a client error") {
            val interceptor = ApiDomainFallbackInterceptor()
            val chain = RecordingChain { request -> response(request, 429) }

            val result = interceptor.intercept(chain)

            Then("it returns the response without fallback") {
                result.code shouldBe 429
                chain.requests.map(HttpRequest::url) shouldBe listOf(PRIMARY_URL)
            }
        }
    }

    Given("a request to another host") {
        val interceptor = ApiDomainFallbackInterceptor()
        val originalUrl = "https://id.vk.ru/oauth2/auth?client_id=1"
        val chain = RecordingChain(
            initialRequest = request(url = originalUrl),
        ) { request -> response(request, 200) }

        When("the request succeeds") {
            interceptor.intercept(chain)

            Then("it leaves the URL unchanged") {
                chain.requests.map(HttpRequest::url) shouldBe listOf(originalUrl)
            }
        }
    }

    Given("an ID request after API fallback") {
        val interceptor = ApiDomainFallbackInterceptor()
        val apiChain = RecordingChain(
            initialRequest = HttpRequest.get(PRIMARY_URL),
        ) { request ->
            if (request.url.startsWith(PRIMARY_ENDPOINT)) {
                throw UnknownHostException("internal-sdk.api.vk.ru")
            }
            response(request, 200)
        }
        interceptor.intercept(apiChain)

        val idUrl = "https://id.vk.ru/oauth2/auth?client_id=1"
        val idChain = RecordingChain(
            initialRequest = HttpRequest.get(idUrl),
        ) { request -> response(request, 200) }

        When("the ID request is executed") {
            interceptor.intercept(idChain)

            Then("it does not use the active API host") {
                idChain.requests.map(HttpRequest::url) shouldBe listOf(idUrl)
            }
        }
    }

    Given("a persisted API fallback domain") {
        val preferences = mockk<SharedPreferences>()
        every { preferences.getString(any(), any()) } answers { secondArg() }
        every { preferences.getString(API_ROUTE_PREFERENCE_KEY, PRIMARY_HOST) } returns FALLBACK_HOST
        val interceptor = ApiDomainFallbackInterceptor(preferences)
        val chain = RecordingChain { request -> response(request, 200) }

        When("the interceptor is created") {
            interceptor.intercept(chain)

            Then("it starts the request from the persisted domain") {
                chain.requests.map(HttpRequest::url) shouldBe listOf(FALLBACK_URL)
            }
        }
    }

    Given("a successful API fallback") {
        val preferences = mockk<SharedPreferences>()
        val editor = mockk<SharedPreferences.Editor>()
        every { preferences.getString(any(), any()) } answers { secondArg() }
        every { preferences.getString(API_ROUTE_PREFERENCE_KEY, PRIMARY_HOST) } returns PRIMARY_HOST
        every { preferences.edit() } returns editor
        every { editor.putString(API_ROUTE_PREFERENCE_KEY, FALLBACK_HOST) } returns editor
        every { editor.apply() } returns Unit
        val interceptor = ApiDomainFallbackInterceptor(preferences)
        val chain = RecordingChain { request ->
            if (request.url.startsWith(PRIMARY_ENDPOINT)) {
                throw UnknownHostException(PRIMARY_HOST)
            }
            response(request, 200)
        }

        When("the reserve domain responds successfully") {
            interceptor.intercept(chain)

            Then("it persists the reserve domain") {
                verify { editor.putString(API_ROUTE_PREFERENCE_KEY, FALLBACK_HOST) }
                verify { editor.apply() }
            }
        }
    }

    Given("the last API fallback domain is active") {
        val preferences = mockk<SharedPreferences>()
        val editor = mockk<SharedPreferences.Editor>()
        every { preferences.getString(any(), any()) } answers { secondArg() }
        every { preferences.getString(API_ROUTE_PREFERENCE_KEY, PRIMARY_HOST) } returns LAST_FALLBACK_HOST
        every { preferences.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.apply() } returns Unit
        val interceptor = ApiDomainFallbackInterceptor(preferences)

        When("both it and the next domain fail because of DNS") {
            val failedChain = RecordingChain { request ->
                throw UnknownHostException(request.url)
            }
            shouldThrow<UnknownHostException> { interceptor.intercept(failedChain) }

            val nextChain = RecordingChain { request -> response(request, 200) }
            val result = interceptor.intercept(nextChain)

            Then("the following request starts from the primary domain") {
                result.code shouldBe 200
                failedChain.requests.map(HttpRequest::url) shouldBe listOf(LAST_FALLBACK_URL, PRIMARY_URL)
                nextChain.requests.map(HttpRequest::url) shouldBe listOf(PRIMARY_URL)
            }
        }
    }
}) {
    private companion object {
        const val PRIMARY_ENDPOINT = "https://api.vk.ru"
        const val FALLBACK_ENDPOINT = "https://internal-sdk.api.vk.ru"
        const val PRIMARY_HOST = "api.vk.ru"
        const val FALLBACK_HOST = "internal-sdk.api.vk.ru"
        const val LAST_FALLBACK_HOST = "api.r.vk.com"
        const val API_ROUTE_PREFERENCE_KEY = "vkid_api_domain"
        const val PRIMARY_URL = "$PRIMARY_ENDPOINT/method/test?key=value"
        const val FALLBACK_URL = "$FALLBACK_ENDPOINT/method/test?key=value"
        const val SECONDARY_FALLBACK_URL = "https://api.l.vk.ru/method/test?key=value"
        const val LAST_FALLBACK_ENDPOINT = "https://$LAST_FALLBACK_HOST"
        const val LAST_FALLBACK_URL = "$LAST_FALLBACK_ENDPOINT/method/test?key=value"
    }
}

private class RecordingChain(
    private val initialRequest: HttpRequest = request(),
    private val responseFor: (HttpRequest) -> HttpResponse,
) : Interceptor.Chain {
    val requests = mutableListOf<HttpRequest>()

    override fun request(): HttpRequest = initialRequest

    override suspend fun proceed(request: HttpRequest): HttpResponse {
        requests += request
        return responseFor(request)
    }

    override fun index(): Int = 0

    override fun size(): Int = 1
}

private fun request(
    url: String = "https://internal-sdk.api.vk.ru/method/test?key=value",
): HttpRequest = HttpRequest.post(
    url = url,
    body = "field=value",
).newBuilder().header("X-Request-Id", "id").build()

private fun response(request: HttpRequest, code: Int): HttpResponse = HttpResponse(
    request = request,
    code = code,
    message = "response",
    body = "",
)
