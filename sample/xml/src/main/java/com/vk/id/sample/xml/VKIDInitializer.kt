@file:OptIn(InternalVKIDApi::class)

package com.vk.id.sample.xml

import android.content.Context
import com.vk.id.VKID
import com.vk.id.common.InternalVKIDApi
import com.vk.id.sample.xml.prefs.captcha.CaptchaHandler
import com.vk.id.sample.xml.prefs.flutter.IsFlutterHandler

public object VKIDInitializer {

    public fun init(
        context: Context,
    ) {
        VKID.init(
            context = context,
            isFlutter = IsFlutterHandler.isFlutter(context),
            captchaRedirectUri = CaptchaHandler.redirectUri,
            forceError14 = CaptchaHandler.forceError14,
            forceHitmanChallenge = CaptchaHandler.forceHitmanChallenge,
        )
    }
}
