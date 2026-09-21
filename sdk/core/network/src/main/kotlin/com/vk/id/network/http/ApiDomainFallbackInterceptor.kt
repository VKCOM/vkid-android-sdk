@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http

import android.content.SharedPreferences
import androidx.core.content.edit
import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.common.ApiConstants
import java.io.IOException
import java.net.URI
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * Routes requests through independently configured domain routes and falls back to their next host.
 */
internal class ApiDomainFallbackInterceptor(
    preferences: SharedPreferences? = null,
) : Interceptor {

    private val routes = listOf(
        DomainRoute(
            hosts = listOf(
                ApiConstants.VK_API_HOST,
                "internal-sdk.api.vk.ru",
                "api.l.vk.ru",
                "api.r.vk.com",
            ),
            preferences = preferences,
            preferenceKey = API_ROUTE_PREFERENCE_KEY,
        ),
        DomainRoute(
            hosts = listOf(
                ApiConstants.VK_ID_HOST,
                "internal-sdk.id.vk.ru",
                "id.l.vk.ru",
                "id.r.vk.com",
            ),
            preferences = preferences,
            preferenceKey = ID_ROUTE_PREFERENCE_KEY,
        ),
    )

    override suspend fun intercept(chain: Interceptor.Chain): HttpResponse {
        val request = chain.request()
        val route = routes.firstOrNull { it.matches(request) }
        if (route == null) {
            return chain.proceed(request)
        }

        val activeRequest = request.withHost(route.activeHost())

        return try {
            chain.proceed(activeRequest)
        } catch (error: IOException) {
            if (isDomainAvailabilityError(error)) {
                chain.proceedFallback(route, activeRequest) ?: throw error
            } else {
                throw error
            }
        }
    }

    private suspend fun Interceptor.Chain.proceedFallback(
        route: DomainRoute,
        request: HttpRequest,
    ): HttpResponse? {
        val fallbackHost = route.nextHost(request.host()) ?: return null
        val fallbackRequest = request.withHost(fallbackHost)
        route.remember(fallbackHost)
        return proceed(fallbackRequest)
    }

    private fun HttpRequest.host(): String = URI(url).host

    private fun HttpRequest.withHost(host: String): HttpRequest {
        val uri = URI(url)
        if (uri.host.equals(host, ignoreCase = true)) {
            return this
        }
        val updatedUrl = URI(
            uri.scheme,
            uri.userInfo,
            host,
            uri.port,
            uri.path,
            uri.query,
            uri.fragment,
        ).toString()
        return newBuilder().url(updatedUrl).build()
    }

    private companion object {
        const val API_ROUTE_PREFERENCE_KEY = "vkid_api_domain"
        const val ID_ROUTE_PREFERENCE_KEY = "vkid_id_domain"
        const val PROXY_CONNECT_503_ERROR = "Unexpected response code for CONNECT: 503"

        fun isDomainAvailabilityError(error: Throwable): Boolean =
            generateSequence(error) { it.cause }.any { cause ->
                cause is SSLException ||
                    cause is UnknownHostException ||
                    (cause is IOException && cause.message == PROXY_CONNECT_503_ERROR)
            }
    }
}

private class DomainRoute(
    private val hosts: List<String>,
    private val preferences: SharedPreferences?,
    private val preferenceKey: String,
) {
    private var activeHost = preferences
        ?.getString(preferenceKey, hosts.first())
        ?.takeIf(::isKnownHost)
        ?: hosts.first()

    fun matches(request: HttpRequest): Boolean = hosts.any { host ->
        host.equals(request.host(), ignoreCase = true)
    }

    @Synchronized
    fun activeHost(): String = activeHost

    @Synchronized
    fun nextHost(currentHost: String): String? {
        if (hosts.size < 2) {
            return null
        }

        val currentIndex = hosts.indexOfFirst { host ->
            host.equals(currentHost, ignoreCase = true)
        }
        val nextIndex = (currentIndex + 1).mod(hosts.size)
        return hosts[nextIndex]
    }

    @Synchronized
    fun remember(host: String) {
        activeHost = host
        preferences?.edit { putString(preferenceKey, host) }
    }

    private fun isKnownHost(host: String): Boolean = hosts.any { knownHost ->
        knownHost.equals(host, ignoreCase = true)
    }

    private fun HttpRequest.host(): String = URI(url).host
}
