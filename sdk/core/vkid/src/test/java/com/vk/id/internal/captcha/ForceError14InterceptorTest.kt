package com.vk.id.internal.captcha

import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.http.HttpRequest
import com.vk.id.network.http.HttpResponse
import com.vk.id.network.http.Interceptor
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(InternalVKIDApi::class)
internal class ForceError14InterceptorTest {

    @Test
    fun `should return fake error 14 response on first call`() = runTest {
        // Given
        val redirectUri = "https://example.com/callback"
        val interceptor = ForceError14Interceptor(redirectUri)
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<HttpRequest>()

        every { chain.request() } returns request
        every { request.url } returns "https://api.vk.com/method/auth"
        coEvery { chain.proceed(request) } returns mockk()

        // When
        val response = interceptor.intercept(chain)

        // Then
        assertEquals(200, response.code)
        assertEquals("OK", response.message)
        assertTrue(response.body?.contains("\"error_code\": 14") == true)
        assertTrue(response.body?.contains("\"redirect_uri\": \"$redirectUri\"") == true)
        assertTrue(response.body?.contains("\"captcha_sid\": \"679747455055\"") == true)
        assertTrue(response.isRequestSuccessful)
    }

    @Test
    fun `should call chain proceed on subsequent calls`() = runTest {
        // Given
        val redirectUri = "https://example.com/callback"
        val interceptor = ForceError14Interceptor(redirectUri)
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<HttpRequest>()
        val expectedResponse = mockk<HttpResponse>()

        every { chain.request() } returns request
        every { request.url } returns "https://api.vk.com/method/auth"
        coEvery { chain.proceed(request) } returns expectedResponse

        // First call - returns fake error 14 response
        interceptor.intercept(chain)

        // When - second call (should call chain.proceed)
        val response = interceptor.intercept(chain)

        // Then - should return the response from chain.proceed
        assertEquals(expectedResponse, response)
        // chain.proceed should be called exactly once on second call
        coVerify(exactly = 1) { chain.proceed(request) }
    }

    @Test
    fun `should work correctly when chain is called multiple times`() = runTest {
        // Given
        val redirectUri = "https://example.com/callback"
        val interceptor = ForceError14Interceptor(redirectUri)
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<HttpRequest>()
        val expectedResponse = mockk<HttpResponse>()

        every { chain.request() } returns request
        every { request.url } returns "https://api.vk.com/method/auth"
        coEvery { chain.proceed(request) } returns expectedResponse

        // When
        val firstResponse = interceptor.intercept(chain) // First call - fake error 14
        val secondResponse = interceptor.intercept(chain) // Second call - chain.proceed

        // Then
        // First response should be fake error 14
        assertEquals(200, firstResponse.code)
        assertTrue(firstResponse.body?.contains("\"error_code\": 14") == true)

        // Second response should be from chain.proceed
        assertEquals(expectedResponse, secondResponse)

        // Verify chain.proceed was called exactly once (on second call)
        coVerify(exactly = 1) { chain.proceed(request) }
    }

    @Test
    fun `should handle null redirect uri`() = runTest {
        // Given
        val interceptor = ForceError14Interceptor(null)
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<HttpRequest>()

        every { chain.request() } returns request
        every { request.url } returns "https://api.vk.com/method/auth"
        coEvery { chain.proceed(request) } returns mockk()

        // When
        val response = interceptor.intercept(chain)

        // Then
        assertEquals(200, response.code)
        assertTrue(response.body?.contains("\"redirect_uri\": \"null\"") == true)
    }

    @Test
    fun `should not call chain proceed on first call`() = runTest {
        // Given
        val redirectUri = "https://example.com/callback"
        val interceptor = ForceError14Interceptor(redirectUri)
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<HttpRequest>()

        every { chain.request() } returns request
        every { request.url } returns "https://api.vk.com/method/auth"
        coEvery { chain.proceed(request) } returns mockk()

        // When - first call
        interceptor.intercept(chain)

        // Then - chain.proceed should NOT be called on first call
        coVerify(exactly = 0) { chain.proceed(request) }
    }
}
