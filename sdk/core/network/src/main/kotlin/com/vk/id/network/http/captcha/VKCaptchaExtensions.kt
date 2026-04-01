@file:OptIn(InternalVKIDApi::class)

package com.vk.id.network.http.captcha

import com.vk.id.captcha.api.VKCaptcha
import com.vk.id.captcha.api.data.VKCaptchaResult
import com.vk.id.captcha.api.listener.VKCaptchaResultListener
import com.vk.id.common.InternalVKIDApi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Suspend версия метода [VKCaptcha.openCaptcha].
 */
@Suppress("TooGenericExceptionCaught")
internal suspend fun VKCaptcha.openCaptchaSuspended(
    domain: String,
    redirectUri: String
): VKCaptchaResult = suspendCancellableCoroutine { continuation ->
    try {
        openCaptcha(
            domain = domain,
            redirectUri = redirectUri,
            listener = object : VKCaptchaResultListener {
                override fun onResult(result: VKCaptchaResult) {
                    if (continuation.isActive) {
                        continuation.resume(result)
                    }
                }
            }
        )

        continuation.invokeOnCancellation {
            closeCaptcha()
        }
    } catch (e: Exception) {
        if (continuation.isActive) {
            continuation.resumeWithException(e)
        }
    }
}

internal fun VKCaptchaResult.toResult(): Result<String> {
    return when (this) {
        is VKCaptchaResult.Success -> Result.success(token)
        is VKCaptchaResult.Error -> Result.failure(
            // Выбрасываем IOException, чтобы корректно обработать в HttpClient
            IOException(error.message, error.error)
        )
    }
}
