package com.vk.id.test

import com.vk.dto.common.id.UserId
import com.vk.id.internal.ipc.SilentAuthInfoProvider
import com.vk.silentauth.SilentAuthInfo

internal class TestSilentAuthInfoProvider : SilentAuthInfoProvider {
    override suspend fun getSilentAuthInfos(timeout: Long): List<SilentAuthInfo> = listOf(
        SilentAuthInfo(
            userId = UserId.DEFAULT,
            uuid = "uuid",
            token = "token",
            expireTime = System.currentTimeMillis() + 1000,
        )
    )

    override fun setAppId(appId: Int) = Unit

    override fun setApiVersion(apiVersion: String) = Unit
}
