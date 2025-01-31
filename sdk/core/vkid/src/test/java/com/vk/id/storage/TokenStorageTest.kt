@file:OptIn(InternalVKIDApi::class)

package com.vk.id.storage

import com.vk.id.AccessToken
import com.vk.id.RefreshToken
import com.vk.id.VKIDUser
import com.vk.id.common.InternalVKIDApi
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify

private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
private val V1_TOKEN_JSON = """{
    |"token":"token",
    |"idToken":"id token",
    |"userID":10,
    |"expireTime":100,
    |"userData":{
        |"firstName":"first",
        |"lastName":"last",
        |"phone":"phone",
        |"photo50":"50",
        |"photo100":"100",
        |"photo200":"200",
        |"email":"email"
    |}
|}
""".trimMargin().replace("\n", "")
private val V1_ACCESS_TOKEN = AccessToken(
    token = "token",
    idToken = "id token",
    userID = 10,
    expireTime = 100,
    userData = VKIDUser(
        firstName = "first",
        lastName = "last",
        phone = "phone",
        photo50 = "50",
        photo100 = "100",
        photo200 = "200",
        email = "email"
    ),
    scopes = null,
)
private val V2_ACCESS_TOKEN_JSON = """{
    |"token":"token",
    |"idToken":"id token",
    |"userID":10,
    |"expireTime":100,
    |"userData":{
        |"firstName":"first",
        |"lastName":"last",
        |"phone":"phone",
        |"photo50":"50",
        |"photo100":"100",
        |"photo200":"200",
        |"email":"email"
    |},
    |"scopes":["phone","email"]
|}
""".trimMargin().replace("\n", "")
private val V2_ACCESS_TOKEN = AccessToken(
    token = "token",
    idToken = "id token",
    userID = 10,
    expireTime = 100,
    userData = VKIDUser(
        firstName = "first",
        lastName = "last",
        phone = "phone",
        photo50 = "50",
        photo100 = "100",
        photo200 = "200",
        email = "email"
    ),
    scopes = setOf("phone", "email"),
)

private const val REFRESH_TOKEN_WITHOUT_SCOPES_KEY = "REFRESH_TOKEN_KEY"
private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_WITH_SCOPES_KEY"
private const val V1_REFRESH_TOKEN_JSON = "refresh token"
private val V1_REFRESH_TOKEN = RefreshToken(
    token = V1_REFRESH_TOKEN_JSON,
    scopes = null,
)
private val V2_REFRESH_TOKEN_JSON = """{
    |"token":"refresh token",
    |"scopes":["phone","email"]
|}
""".trimMargin().replace("\n", "")
private val V2_REFRESH_TOKEN = RefreshToken(
    token = "refresh token",
    scopes = setOf("phone", "email"),
)

internal class TokenStorageTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    Given("A token storage") {
        val prefs = mockk<InternalVKIDEncryptedSharedPreferencesStorage>()
        val storage = TokenStorage(prefs)
        When("Saves access token") {
            every { prefs.set(ACCESS_TOKEN_KEY, V2_ACCESS_TOKEN_JSON) } just runs
            storage.accessToken = V2_ACCESS_TOKEN
            Then("Saves serialized token to prefs") {
                verify { prefs.set(ACCESS_TOKEN_KEY, V2_ACCESS_TOKEN_JSON) }
            }
        }
        When("Requests empty access token") {
            every { prefs.getString(ACCESS_TOKEN_KEY) } returns null
            Then("Access token is null") {
                storage.accessToken.shouldBeNull()
            }
        }
        When("Requests malformed access token") {
            every { prefs.getString(ACCESS_TOKEN_KEY) } returns "wrong token"
            Then("Access token is null") {
                storage.accessToken.shouldBeNull()
            }
        }
        When("Request v1 access token") {
            every { prefs.getString(ACCESS_TOKEN_KEY) } returns V1_TOKEN_JSON
            Then("Access token has null scopes") {
                storage.accessToken shouldBe V1_ACCESS_TOKEN
            }
        }
        When("Requests correct access token") {
            every { prefs.getString(ACCESS_TOKEN_KEY) } returns V2_ACCESS_TOKEN_JSON
            Then("Access token is deserialized") {
                storage.accessToken shouldBe V2_ACCESS_TOKEN
            }
        }

        When("Saves refresh token") {
            every { prefs.set(REFRESH_TOKEN_KEY, V2_REFRESH_TOKEN_JSON) } just runs
            storage.refreshToken = V2_REFRESH_TOKEN
            Then("Saves serialized token to prefs") {
                verify { prefs.set(REFRESH_TOKEN_KEY, V2_REFRESH_TOKEN_JSON) }
            }
        }
        When("Requests empty refresh token") {
            every { prefs.getString(REFRESH_TOKEN_KEY) } returns null
            And("Old token is empty as well") {
                every { prefs.getString(REFRESH_TOKEN_WITHOUT_SCOPES_KEY) } returns null
                Then("Refresh token is null") {
                    storage.refreshToken.shouldBeNull()
                }
            }
            And("Old token has value") {
                every { prefs.getString(REFRESH_TOKEN_WITHOUT_SCOPES_KEY) } returns V1_REFRESH_TOKEN_JSON
                Then("Refresh token has null scopes") {
                    storage.refreshToken shouldBe V1_REFRESH_TOKEN
                }
            }
        }
        When("Requests refresh token") {
            every { prefs.getString(REFRESH_TOKEN_KEY) } returns V2_REFRESH_TOKEN_JSON
            Then("Refresh token is deserialized") {
                storage.refreshToken shouldBe V2_REFRESH_TOKEN
            }
        }
        When("Refresh token is malformed") {
            every { prefs.getString(REFRESH_TOKEN_KEY) } returns "malformed json"
            Then("Refresh token is null") {
                storage.refreshToken.shouldBeNull()
            }
        }

        When("Storage is cleared") {
            every { prefs.set(ACCESS_TOKEN_KEY, null) } just runs
            every { prefs.set(REFRESH_TOKEN_KEY, null) } just runs
            every { prefs.set(REFRESH_TOKEN_WITHOUT_SCOPES_KEY, null) } just runs
            storage.clear()
            Then("Prefs for all keys are cleared") {
                verify { prefs.set(ACCESS_TOKEN_KEY, null) }
                verify { prefs.set(REFRESH_TOKEN_KEY, null) }
                verify { prefs.set(REFRESH_TOKEN_WITHOUT_SCOPES_KEY, null) }
            }
        }
    }
})
