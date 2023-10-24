package com.vk.id.onetap.xml

import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal fun startAuth(
    coroutineScope: CoroutineScope,
    updateState: ((VKIDButtonState) -> VKIDButtonState) -> Unit,
    vkid: VKID,
    onAuth: (AccessToken) -> Unit,
    onFail: (VKIDAuthFail) -> Unit,
) {
    coroutineScope.launch {
        updateState { it.copy(inProgress = true) }
        vkid.authorize(object : VKID.AuthCallback {
            override fun onSuccess(accessToken: AccessToken) {
                updateState { it.copy(inProgress = false) }
                onAuth(accessToken)
            }

            override fun onFail(fail: VKIDAuthFail) {
                updateState { it.copy(inProgress = false) }
                onFail(fail)
            }
        })
    }
}
