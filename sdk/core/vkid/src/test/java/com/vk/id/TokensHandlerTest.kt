@file:OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class, InternalVKIDApi::class)

package com.vk.id

import com.vk.id.common.InternalVKIDApi
import com.vk.id.fetchuser.VKIDUserInfoFetcher
import com.vk.id.internal.auth.VKIDTokenPayload
import com.vk.id.internal.concurrent.VKIDCoroutinesDispatchers
import com.vk.id.storage.TokenStorage
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.testCoroutineScheduler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

private const val ACCESS_TOKEN_VALUE = "access token"
private const val REFRESH_TOKEN_VALUE = "refresh token"
private const val ID_TOKEN = "id token"
private const val EXPIRES_IN = 0L
private const val USER_ID = 100L
private const val FIRST_NAME = "first"
private const val LAST_NAME = "last"
private const val PHONE = "phone"
private const val AVATAR = "avatar"
private const val EMAIL = "email"
private const val STATE = "state"

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
    token = ACCESS_TOKEN_VALUE,
    idToken = ID_TOKEN,
    userID = USER_ID,
    expireTime = -1,
    userData = VKID_USER,
    scopes = setOf("phone", "email"),
)
private val REFRESH_TOKEN = RefreshToken(
    token = REFRESH_TOKEN_VALUE,
    scopes = setOf("phone", "email"),
)
private val TOKEN_PAYLOAD = VKIDTokenPayload(
    accessToken = ACCESS_TOKEN_VALUE,
    refreshToken = REFRESH_TOKEN_VALUE,
    idToken = ID_TOKEN,
    expiresIn = EXPIRES_IN,
    userId = USER_ID,
    state = STATE,
    scope = "phone email",
)

internal class TokensHandlerTest : BehaviorSpec({

    coroutineTestScope = true
    isolationMode = IsolationMode.InstancePerLeaf

    Given("Tokens handler") {
        val userInfoFetcher = mockk<VKIDUserInfoFetcher>()
        val tokenStorage = mockk<TokenStorage>()
        val scheduler = testCoroutineScheduler
        val dispatchers = mockk<VKIDCoroutinesDispatchers>()
        val testDispatcher = StandardTestDispatcher(scheduler)
        every { dispatchers.io } returns testDispatcher
        val handler = TokensHandler(
            userInfoFetcher = userInfoFetcher,
            tokenStorage = tokenStorage,
            dispatchers = dispatchers,
        )
        When("Handles token payload") {
            val onSuccess = mockk<(AccessToken) -> Unit>()
            val onFailedApiCall = mockk<(Throwable) -> Unit>()
            val capturedOnSuccess = slot<suspend (VKIDUser) -> Unit>()
            coEvery {
                userInfoFetcher.fetch(
                    ACCESS_TOKEN_VALUE,
                    capture(capturedOnSuccess),
                    onFailedApiCall,
                )
            } just runs
            every { tokenStorage.accessToken = ACCESS_TOKEN } just runs
            every { tokenStorage.refreshToken = REFRESH_TOKEN } just runs
            every { onSuccess(ACCESS_TOKEN) } just runs
            runTest(scheduler) {
                handler.handle(
                    payload = TOKEN_PAYLOAD,
                    onSuccess = onSuccess,
                    onFailedApiCall = onFailedApiCall,
                )
            }
            Then("Call user info fetcher") {
                coVerify {
                    userInfoFetcher.fetch(
                        ACCESS_TOKEN_VALUE,
                        capture(capturedOnSuccess),
                        onFailedApiCall,
                    )
                }
                runTest(scheduler) {
                    capturedOnSuccess.captured(VKID_USER)
                }
                verify { tokenStorage.accessToken = ACCESS_TOKEN }
                verify { tokenStorage.refreshToken = REFRESH_TOKEN }
                verify { onSuccess(ACCESS_TOKEN) }
            }
        }
        When("Handles token payload with blank id token payload") {
            val onSuccess = mockk<(AccessToken) -> Unit>()
            val onFailedApiCall = mockk<(Throwable) -> Unit>()
            val capturedOnSuccess = slot<suspend (VKIDUser) -> Unit>()
            coEvery {
                userInfoFetcher.fetch(
                    ACCESS_TOKEN_VALUE,
                    capture(capturedOnSuccess),
                    onFailedApiCall,
                )
            } just runs
            every { tokenStorage.accessToken } returns ACCESS_TOKEN
            every { tokenStorage.accessToken = ACCESS_TOKEN } just runs
            every { tokenStorage.refreshToken = REFRESH_TOKEN } just runs
            every { onSuccess(ACCESS_TOKEN) } just runs
            runTest(scheduler) {
                handler.handle(
                    payload = TOKEN_PAYLOAD.copy(idToken = "  "),
                    onSuccess = onSuccess,
                    onFailedApiCall = onFailedApiCall,
                )
            }
            Then("Call user info fetcher") {
                coVerify {
                    userInfoFetcher.fetch(
                        ACCESS_TOKEN_VALUE,
                        capture(capturedOnSuccess),
                        onFailedApiCall,
                    )
                }
                runTest(scheduler) {
                    capturedOnSuccess.captured(VKID_USER)
                }
                verify { tokenStorage.accessToken = ACCESS_TOKEN }
                verify { tokenStorage.refreshToken = REFRESH_TOKEN }
                verify { onSuccess(ACCESS_TOKEN) }
            }
        }
        When("Handles token payload without refreshing access token") {
            val onSuccess = mockk<(AccessToken) -> Unit>()
            val onFailedApiCall = mockk<(Throwable) -> Unit>()
            val capturedOnSuccess = slot<suspend (VKIDUser) -> Unit>()
            coEvery {
                userInfoFetcher.fetch(
                    ACCESS_TOKEN_VALUE,
                    capture(capturedOnSuccess),
                    onFailedApiCall,
                )
            } just runs
            every { tokenStorage.refreshToken = REFRESH_TOKEN } just runs
            every { onSuccess(ACCESS_TOKEN) } just runs
            runTest(scheduler) {
                handler.handle(
                    payload = TOKEN_PAYLOAD,
                    onSuccess = onSuccess,
                    onFailedApiCall = onFailedApiCall,
                    refreshAccessToken = false,
                )
            }
            Then("Call user info fetcher") {
                coVerify {
                    userInfoFetcher.fetch(
                        ACCESS_TOKEN_VALUE,
                        capture(capturedOnSuccess),
                        onFailedApiCall,
                    )
                }
                runTest(scheduler) {
                    capturedOnSuccess.captured(VKID_USER)
                }
                verify(exactly = 0) { tokenStorage.accessToken = ACCESS_TOKEN }
                verify { tokenStorage.refreshToken = REFRESH_TOKEN }
                verify { onSuccess(ACCESS_TOKEN) }
            }
        }
    }
})
