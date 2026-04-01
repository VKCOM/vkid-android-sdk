package com.vk.id.network.http

import android.content.Context
import com.vk.id.common.InternalVKIDApi
import com.vk.id.logger.InternalVKIDLog
import com.vk.id.network.BuildConfig
import com.vk.id.network.http.ssl.SSLConfigurator
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.StandardCharsets
import kotlin.coroutines.cancellation.CancellationException

@InternalVKIDApi
@Suppress("TooManyFunctions")
public class HttpClient internal constructor(private val config: Config) {

    @OptIn(InternalVKIDApi::class)
    internal data class Config(
        val context: Context,
        val connectingTimeout: Int,
        val readingTimeout: Int,
        val followRedirects: Boolean,
        val maxRedirects: Int,
        val applicationInterceptors: List<Interceptor> = emptyList(),
    )

    private val logger = InternalVKIDLog.createLoggerForTag("HttpClient")

    private val sslConfigurator: SSLConfigurator by lazy {
        SSLConfigurator(config.context)
    }

    private val interceptorManager by lazy {
        InterceptorManager(
            httpClient = this,
            applicationInterceptors = config.applicationInterceptors,
        )
    }

    @Suppress("TooGenericExceptionCaught")
    public fun executeRequest(request: HttpRequest): Result<HttpResponse> =
        try {
            val response = runBlocking { executeWithInterceptors(request) }
            Result.success(response)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Throwable) {
            Result.failure(exception)
        }

    /**
     * Выполняет реальный HTTP запрос без интерсепторов.
     * Используется только HttpExecutionInterceptor.
     */
    @Throws
    internal fun executeHttpRequest(request: HttpRequest): HttpResponse {
        return executeHttpRequestInternal(
            request = request,
            redirectCount = 0
        )
    }

    @Throws
    internal suspend fun executeWithInterceptors(request: HttpRequest): HttpResponse {
        return interceptorManager.proceed(request)
    }

    @Suppress("LongParameterList")
    private fun executeHttpRequestInternal(
        request: HttpRequest,
        redirectCount: Int,
    ): HttpResponse {
        checkRedirectLimit(redirectCount)
        val requestUrl = createUrlOrThrow(request.url)

        var connection: HttpURLConnection? = null
        return try {
            connection = createConnection(requestUrl)

            if (requestUrl.protocol == "https") {
                sslConfigurator.configureSSL(connection)
            }

            configureConnection(connection, request)
            sendRequestBody(connection, request.body)

            val response = connection.getResponse(request)

            handleRedirectIfNeeded(connection, response, request.method, request.headers, redirectCount)
                ?: response
        } catch (e: IOException) {
            logAndThrowNetworkError(request.url, e)
        } finally {
            connection?.disconnect()
        }
    }

    private fun checkRedirectLimit(redirectCount: Int) {
        require(redirectCount <= config.maxRedirects) {
            "Too many redirects (max: ${config.maxRedirects})"
        }
    }

    private fun createUrlOrThrow(url: String): URL {
        return try {
            URL(url)
        } catch (e: MalformedURLException) {
            throw IllegalArgumentException("Invalid URL: $url", e)
        }
    }

    private fun createConnection(requestUrl: URL): HttpURLConnection {
        require(requestUrl.protocol == "https" || (BuildConfig.DEBUG && requestUrl.protocol == "http")) {
            "Only HTTPS protocol is supported"
        }
        return requestUrl.openConnection() as HttpURLConnection
    }

    @Suppress("ReturnCount")
    private fun handleRedirectIfNeeded(
        connection: HttpURLConnection,
        response: HttpResponse,
        method: String,
        headers: Map<String, String>,
        redirectCount: Int,
    ): HttpResponse? {
        if (!config.followRedirects || !isRedirect(response.code)) {
            return null
        }

        val location = connection.getHeaderField("Location") ?: return null
        logger.debug("Redirecting to: $location (status: ${response.code})")
        connection.disconnect()

        return executeHttpRequestInternal(
            HttpRequest(
                method = method,
                url = location,
                body = null,
                headers = headers,
                contentType = null,
            ),
            redirectCount = redirectCount + 1
        )
    }

    private fun logAndThrowNetworkError(url: String, e: IOException): Nothing {
        logger.error("Network error during request to $url", e)
        throw IOException("Network error: ${e.message}", e)
    }

    private fun configureConnection(
        connection: HttpURLConnection,
        request: HttpRequest
    ) {
        connection.apply {
            connectTimeout = config.connectingTimeout
            readTimeout = config.readingTimeout
            requestMethod = request.method.uppercase()
            useCaches = false

            request.headers.forEach { (key, value) ->
                setRequestProperty(key, value)
            }

            request.contentType?.let {
                setRequestProperty("Content-Type", it)
            }
        }
    }

    private fun sendRequestBody(
        connection: HttpURLConnection,
        body: String?,
    ) {
        val requestBody = body ?: return

        try {
            connection.doOutput = true
            connection.outputStream.use { outStream ->
                outStream.write(requestBody.toByteArray(StandardCharsets.UTF_8))
                outStream.flush()
            }
        } catch (e: IOException) {
            logger.error("Failed to send request body", e)
            throw IOException("Failed to send request body", e)
        }
    }

    private fun HttpURLConnection.getResponse(request: HttpRequest): HttpResponse {
        val headers = mutableMapOf<String, String>()
        headerFields.forEach { (key, values) ->
            if (key != null && values.isNotEmpty()) {
                headers[key] = values.first()
            }
        }

        val body = try {
            readResponseBody()
        } catch (e: IOException) {
            logger.error("Failed to read response body", e)
            ""
        }

        return HttpResponse(
            request = request,
            code = responseCode,
            message = responseMessage,
            body = body,
            headers = headers,
            isRequestSuccessful = responseCode in 200..299
        )
    }

    private fun HttpURLConnection.readResponseBody(): String {
        val streamToRead = if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
            errorStream
        } else {
            inputStream
        } ?: return ""

        return try {
            BufferedReader(
                InputStreamReader(streamToRead, StandardCharsets.UTF_8)
            ).use { reader ->
                reader.readText()
            }
        } catch (e: IOException) {
            logger.error("Failed to read response body", e)
            ""
        }
    }

    private companion object {
        private val REDIRECT_STATUSES = setOf(
            HttpURLConnection.HTTP_MOVED_PERM, // 301
            HttpURLConnection.HTTP_MOVED_TEMP, // 302
            HttpURLConnection.HTTP_SEE_OTHER, // 303
            HttpURLConnection.HTTP_USE_PROXY, // 305
            307, // Temporary Redirect
            308 // Permanent Redirect
        )

        fun isRedirect(statusCode: Int): Boolean = statusCode in REDIRECT_STATUSES
    }
}
