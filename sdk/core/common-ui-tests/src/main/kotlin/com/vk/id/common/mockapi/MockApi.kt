@file:OptIn(InternalVKIDApi::class)

package com.vk.id.common.mockapi

import com.vk.id.VKIDUser
import com.vk.id.common.InternalVKIDApi
import com.vk.id.test.VKIDTestBuilder
import com.vk.id.test.VKIDTokenPayloadResponse

public object MockApi {
    public const val ACCESS_TOKEN: String = "access token"
    public const val EXPIRES_IN: Long = 1000L
    public const val USER_ID: Long = 0L
    public const val EMAIL: String = "email"
    public const val PHONE: String = "phone"
    public const val PHONE_ACCESS_KEY: String = "phone access key"

    private const val FIRST_NAME: String = "first name"
    private const val LAST_NAME: String = "last name"
    private const val AVATAR: String = "avatar"

    public fun mockApiUser(): VKIDUser = VKIDUser(
        firstName = FIRST_NAME,
        lastName = LAST_NAME,
        photo200 = AVATAR
    )

    public fun mockReturnedUser(): VKIDUser = VKIDUser(
        firstName = FIRST_NAME,
        lastName = LAST_NAME,
        phone = PHONE,
        photo50 = null,
        photo100 = null,
        photo200 = AVATAR,
        email = EMAIL
    )
}

public fun VKIDTestBuilder.mockApiError(): VKIDTestBuilder = getTokenResponse(
    Result.failure(UnsupportedOperationException("fake error"))
)

public fun VKIDTestBuilder.mockApiSuccess(): VKIDTestBuilder = getTokenResponse(
    Result.success(
        VKIDTokenPayloadResponse(
            accessToken = MockApi.ACCESS_TOKEN,
            expiresIn = MockApi.EXPIRES_IN,
            userId = MockApi.USER_ID,
            email = MockApi.EMAIL,
            phone = MockApi.PHONE,
            phoneAccessKey = MockApi.PHONE_ACCESS_KEY,
        )
    )
)
