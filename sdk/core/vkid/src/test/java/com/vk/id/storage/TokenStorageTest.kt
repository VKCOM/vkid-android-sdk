@file:OptIn(InternalVKIDApi::class)

package com.vk.id.storage

import com.vk.id.AccessToken
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
private val ACCESS_TOKEN = AccessToken(
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
    )
)
private val TOKEN_JSON = """{
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

private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"
private const val REFRESH_TOKEN = "refresh token"

private const val ID_TOKEN_KEY = "ID_TOKEN_KEY"
private const val ID_TOKEN = "id token"

internal class TokenStorageTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    Given("A token storage") {
        val prefs = mockk<InternalVKIDEncryptedSharedPreferencesStorage>()
        val storage = TokenStorage(prefs)
        When("Saves access token") {
            every { prefs.set(ACCESS_TOKEN_KEY, TOKEN_JSON) } just runs
            storage.accessToken = ACCESS_TOKEN
            Then("Saves serialized token to prefs") {
                verify { prefs.set(ACCESS_TOKEN_KEY, TOKEN_JSON) }
            }
        }
        When("Requests empty access token") {
            every { prefs.getString(ACCESS_TOKEN_KEY) } returns null
            Then("Access token is null") {
                storage.accessToken.shouldBeNull()
            }
        }
        When("Requests wrong access token") {
            every { prefs.getString(ACCESS_TOKEN_KEY) } returns "wrong token"
            Then("Access token is null") {
                storage.accessToken.shouldBeNull()
            }
        }
        When("Requests correct access token") {
            every { prefs.getString(ACCESS_TOKEN_KEY) } returns TOKEN_JSON
            Then("Access token is deserialized") {
                storage.accessToken shouldBe ACCESS_TOKEN
            }
        }

        When("Saves refresh token") {
            every { prefs.set(REFRESH_TOKEN_KEY, REFRESH_TOKEN) } just runs
            storage.refreshToken = REFRESH_TOKEN
            Then("Saves serialized token to prefs") {
                verify { prefs.set(REFRESH_TOKEN_KEY, REFRESH_TOKEN) }
            }
        }
        When("Requests empty refresh token") {
            every { prefs.getString(REFRESH_TOKEN_KEY) } returns null
            Then("Refresh token is null") {
                storage.refreshToken.shouldBeNull()
            }
        }
        When("Requests refresh token") {
            every { prefs.getString(REFRESH_TOKEN_KEY) } returns REFRESH_TOKEN
            Then("Refresh token is deserialized") {
                storage.refreshToken shouldBe REFRESH_TOKEN
            }
        }

        When("Saves id token") {
            every { prefs.set(ID_TOKEN_KEY, ID_TOKEN) } just runs
            storage.idToken = ID_TOKEN
            Then("Saves serialized token to prefs") {
                verify { prefs.set(ID_TOKEN_KEY, ID_TOKEN) }
            }
        }
        When("Requests empty id token") {
            every { prefs.getString(ID_TOKEN_KEY) } returns null
            Then("id token is null") {
                storage.idToken.shouldBeNull()
            }
        }
        When("Requests id token") {
            every { prefs.getString(ID_TOKEN_KEY) } returns ID_TOKEN
            Then("id token is deserialized") {
                storage.idToken shouldBe ID_TOKEN
            }
        }

        When("Storage is cleared") {
            every { prefs.set(ACCESS_TOKEN_KEY, null) } just runs
            every { prefs.set(REFRESH_TOKEN_KEY, null) } just runs
            every { prefs.set(ID_TOKEN_KEY, null) } just runs
            storage.clear()
            Then("Prefs for all keys are cleared") {
                verify { prefs.set(ACCESS_TOKEN_KEY, null) }
                verify { prefs.set(REFRESH_TOKEN_KEY, null) }
                verify { prefs.set(ID_TOKEN_KEY, null) }
            }
        }
    }
})
