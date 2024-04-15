@file:OptIn(InternalVKIDApi::class)

package com.vk.id.common.mockapi

import com.vk.id.VKIDUser
import com.vk.id.common.InternalVKIDApi
import com.vk.id.test.VKIDTestBuilder
import com.vk.id.test.VKIDTokenPayloadResponse
import com.vk.id.test.VKIDUserInfoPayloadResponse
import com.vk.id.test.VKIDUserPayloadResponse

public object MockApi {
    public const val ACCESS_TOKEN: String = "access token"
    public const val REFRESH_TOKEN: String = "refresh token"
    public const val ID_TOKEN: String = "id token"
    public const val EXPIRES_IN: Long = 1000L
    public const val USER_ID: Long = 0L
    public const val EMAIL: String = "email"
    public const val PHONE: String = "phone"
    public const val PHONE_ACCESS_KEY: String = "phone access key"
    public const val FIRST_NAME: String = "first name"
    public const val LAST_NAME: String = "last name"
    public const val AVATAR: String = "avatar"
    public const val STATE: String = "state"

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

public fun VKIDTestBuilder.mockGetTokenSuccess(): VKIDTestBuilder = this
    .getTokenResponse(
        Result.success(
            VKIDTokenPayloadResponse(
                accessToken = MockApi.ACCESS_TOKEN,
                refreshToken = MockApi.ACCESS_TOKEN,
                idToken = MockApi.ID_TOKEN,
                expiresIn = MockApi.EXPIRES_IN,
                userId = MockApi.USER_ID,
            )
        )
    )

public fun VKIDTestBuilder.mockUserInfoError(): VKIDTestBuilder = this
    .getUserInfoResponse(Result.success(VKIDUserInfoPayloadResponse(error = "mock error")))

public fun VKIDTestBuilder.mockApiError(): VKIDTestBuilder = this
    .getTokenResponse(Result.failure(UnsupportedOperationException("fake error")))
    .mockUserInfoError()

public fun VKIDTestBuilder.mockApiSuccess(): VKIDTestBuilder = this
    .mockGetTokenSuccess()
    .getUserInfoResponse(
        Result.success(
            VKIDUserInfoPayloadResponse(
                user = VKIDUserPayloadResponse(
                    firstName = MockApi.FIRST_NAME,
                    lastName = MockApi.LAST_NAME,
                    email = MockApi.EMAIL,
                    phone = MockApi.PHONE,
                    avatar = MockApi.AVATAR,
                ),
                state = MockApi.STATE
            )
        )
    )
