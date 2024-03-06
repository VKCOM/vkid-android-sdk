package com.vk.id

import com.vk.id.fetchuser.VKIDUserInfoFetcher
import com.vk.id.internal.auth.VKIDTokenPayload
import com.vk.id.storage.TokenStorage
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify

private const val ACCESS_TOKEN_VALUE = "access token"
private const val REFRESH_TOKEN = "refresh token"
private const val EXPIRES_IN = 0L
private const val USER_ID = 100L
private const val FIRST_NAME = "first"
private const val LAST_NAME = "last"
private const val PHONE = "phone"
private const val AVATAR = "avatar"
private const val EMAIL = "email"

private val VKID_USER = VKIDUser(
    firstName = FIRST_NAME,
    lastName = LAST_NAME,
    phone = PHONE,
    photo50 = null,
    photo100 = null,
    photo200 = AVATAR,
    email = EMAIL,
)
private val ACCESS_TOKEN = AccessToken(
    ACCESS_TOKEN_VALUE,
    USER_ID,
    -1,
    VKID_USER,
)
private val TOKEN_PAYLOAD = VKIDTokenPayload(
    ACCESS_TOKEN_VALUE,
    REFRESH_TOKEN,
    EXPIRES_IN,
    USER_ID,
)

internal class TokensHandlerTest : BehaviorSpec({
    Given("Tokens handler") {
        val userInfoFetcher = mockk<VKIDUserInfoFetcher>()
        val tokenStorage = mockk<TokenStorage>()
        val handler = TokensHandler(
            userInfoFetcher = userInfoFetcher,
            tokenStorage = tokenStorage,
        )
        When("Handles token payload") {
            val onSuccess = mockk<(AccessToken) -> Unit>()
            val onFailedApiCall = mockk<(Throwable) -> Unit>()
            val onFailedOAuthState = mockk<() -> Unit>()
            val capturedOnSuccess = slot<(VKIDUser) -> Unit>()
            coEvery {
                userInfoFetcher.fetch(
                    ACCESS_TOKEN_VALUE,
                    capture(capturedOnSuccess),
                    onFailedApiCall,
                    onFailedOAuthState,
                )
            } just runs
            every { tokenStorage.accessToken = ACCESS_TOKEN } just runs
            every { tokenStorage.refreshToken = REFRESH_TOKEN } just runs
            every { onSuccess(ACCESS_TOKEN) } just runs
            handler.handle(
                payload = TOKEN_PAYLOAD,
                onSuccess = onSuccess,
                onFailedApiCall = onFailedApiCall,
                onFailedOAuthState = onFailedOAuthState,
            )
            Then("Call user info fetcher") {
                coVerify {
                    userInfoFetcher.fetch(
                        ACCESS_TOKEN_VALUE,
                        capture(capturedOnSuccess),
                        onFailedApiCall,
                        onFailedOAuthState,
                    )
                }
                capturedOnSuccess.captured(VKID_USER)
                verify { tokenStorage.accessToken = ACCESS_TOKEN }
                verify { tokenStorage.refreshToken = REFRESH_TOKEN }
                verify { onSuccess(ACCESS_TOKEN) }
            }
        }
    }
})
