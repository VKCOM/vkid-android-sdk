@file:OptIn(InternalVKIDApi::class)

package com.vk.id.internal.captcha

import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.http.HttpResponse
import com.vk.id.network.http.Interceptor
import java.util.concurrent.atomic.AtomicBoolean

internal class ForceError14Interceptor(
    private val redirectUri: String?
) : Interceptor {

    private val once = AtomicBoolean(true)

    override suspend fun intercept(chain: Interceptor.Chain): HttpResponse {
        if (!once.getAndSet(false)) {
            return chain.proceed(chain.request())
        }

        val request = chain.request()

        val fakeResponseBody = """
                            {
                                "error": {
                                    "error_code": 14,
                                    "error_msg": "Captcha needed",
                                    "request_params": [
                                    ],
                                    "redirect_uri": "$redirectUri",
                                    "captcha_sid": "679747455055",
                                    "is_refresh_enabled": true,
                                    "captcha_img": "https:\/\/vk.ru\/captcha.php?sid=679747455055&source=check_user_action_validate%2Bmail_send&app_id=6287487&device_id=&s=1&resized=1",
                                    "captcha_ts": 1741099026.324000,
                                    "captcha_attempt": 1,
                                    "captcha_ratio": 2.600000,
                                    "is_sound_captcha_available": true,
                                    "captcha_track": "https:\/\/vk.ru\/sound_captcha.php?captcha_sid=679747455055&act=get&source=check_user_action_validate%2Bmail_send&app_id=6287487&device_id=",
                                    "uiux_changes": true
                                }
                            }
        """.trimIndent()

        return HttpResponse(
            request = request,
            code = @Suppress("MagicNumber") 200,
            message = "OK",
            body = fakeResponseBody,
            headers = mapOf("Content-Type" to "application/json; charset=utf-8"),
            isRequestSuccessful = true
        )
    }
}
