package com.vk.id.internal.captcha

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.util.concurrent.atomic.AtomicBoolean

internal class ForceError14Interceptor(
    private val redirectUri: String?
) : Interceptor {

    private val once = AtomicBoolean(true)

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!once.getAndSet(false)) {
            return chain.proceed(chain.request())
        }
        return Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_2)
            .message("OK")
            .body(
                """
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
                """.trimIndent().toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull())
            )
            .code(@Suppress("MagicNumber") 200)
            .build()
    }
}
