@file:OptIn(InternalVKIDApi::class)

package com.vk.id.common.mockapi

import com.vk.id.VKIDUser
import com.vk.id.common.InternalVKIDApi
import com.vk.id.network.groupsubscription.data.InternalVKIDGroupByIdData
import com.vk.id.network.groupsubscription.data.InternalVKIDGroupMembersData
import com.vk.id.test.InternalVKIDLogoutPayloadResponse
import com.vk.id.test.InternalVKIDTestBuilder
import com.vk.id.test.InternalVKIDTokenPayloadResponse
import com.vk.id.test.InternalVKIDUserInfoPayloadResponse
import com.vk.id.test.InternalVKIDUserPayloadResponse
import java.io.IOException

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

public fun InternalVKIDTestBuilder.mockGetTokenSuccess(): InternalVKIDTestBuilder = this
    .getTokenResponse(
        Result.success(
            InternalVKIDTokenPayloadResponse(
                accessToken = MockApi.ACCESS_TOKEN,
                refreshToken = MockApi.ACCESS_TOKEN,
                idToken = MockApi.ID_TOKEN,
                expiresIn = MockApi.EXPIRES_IN,
                userId = MockApi.USER_ID,
                scope = "phone email",
            )
        )
    )

public fun InternalVKIDTestBuilder.mockUserInfoError(): InternalVKIDTestBuilder = this
    .getUserInfoResponse(Result.success(InternalVKIDUserInfoPayloadResponse(error = "mock error")))

public fun InternalVKIDTestBuilder.mockLogoutError(): InternalVKIDTestBuilder = this
    .logoutResponse(Result.success(InternalVKIDLogoutPayloadResponse(error = "error")))

public fun InternalVKIDTestBuilder.mockApiError(): InternalVKIDTestBuilder = this
    .getTokenResponse(Result.failure(UnsupportedOperationException("fake error")))
    .mockUserInfoError()

public fun InternalVKIDTestBuilder.mockApiSuccess(): InternalVKIDTestBuilder = this
    .mockGetTokenSuccess()
    .getUserInfoResponse(
        Result.success(
            InternalVKIDUserInfoPayloadResponse(
                user = InternalVKIDUserPayloadResponse(
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

public fun InternalVKIDTestBuilder.getGroupFail(): InternalVKIDTestBuilder = getGroupResponse(
    Result.failure(IOException("Fake api error"))
)

public fun InternalVKIDTestBuilder.getGroupSuccess(): InternalVKIDTestBuilder = getGroupResponse(
    Result.success(
        InternalVKIDGroupByIdData(
            groupId = "1",
            name = "",
            imageUrl = "",
            description = "",
            isVerified = true,
        )
    )
)

public fun InternalVKIDTestBuilder.getMembersSuccess(): InternalVKIDTestBuilder = getMembersResponses(
    response1 = Result.success(
        InternalVKIDGroupMembersData(
            count = 1,
            members = listOf()
        )
    ),
    response2 = Result.success(
        InternalVKIDGroupMembersData(
            count = 1,
            members = listOf()
        )
    )
)
