@file:OptIn(InternalVKIDApi::class)

package com.vk.id.common.baseauthtest

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.activity.AutoTestActivityRule
import com.vk.id.common.allure.Owners
import com.vk.id.common.allure.Platform
import com.vk.id.common.allure.Priority
import com.vk.id.common.allure.Product
import com.vk.id.common.allure.Project
import com.vk.id.common.basetest.BaseUiTest
import com.vk.id.common.mockapi.MockApi
import com.vk.id.common.mockapi.mockApiError
import com.vk.id.common.mockapi.mockApiSuccess
import com.vk.id.common.mockapi.mockGetTokenSuccess
import com.vk.id.common.mockapi.mockLogoutError
import com.vk.id.common.mockapi.mockUserInfoError
import com.vk.id.common.mockprovider.ContinueAuthScenario
import com.vk.id.test.InternalVKIDTestBuilder
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.qameta.allure.kotlin.Allure
import io.qameta.allure.kotlin.Owner
import org.junit.Before
import org.junit.Rule
import java.util.UUID

@Platform(Platform.ANDROID_AUTO)
@Product(Product.VKID_SDK)
@Project(Project.VKID_SDK)
@Owner(Owners.DANIIL_KLIMCHUK)
@Suppress("TooManyFunctions")
@Priority(Priority.CRITICAL)
public abstract class BaseAuthTest(
    private val oAuth: OAuth?,
    private val skipTest: Boolean = false,
) : BaseUiTest() {

    private companion object {
        val DEVICE_ID = UUID.randomUUID().toString()
        val AUTH_CODE = AuthCodeData(code = "d654574949e8664ba1", deviceId = DEVICE_ID)
    }

    @get:Rule
    public val composeTestRule: AutoTestActivityRule = createAndroidComposeRule()

    @Before
    public fun setAllureParam() {
        if (!skipTest) {
            oAuth?.let { Allure.parameter("oauth", it.name) }
        }
    }

    public open fun tokenIsReceived(): Unit = runIfShouldNotSkip {
        var accessToken: AccessToken? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        before {
            vkidBuilder()
                .mockApiSuccess()
                .user(MockApi.mockApiUser())
                .overrideDeviceId(DEVICE_ID)
                .build()
            setContent(
                onAuth = { oAuth, token ->
                    receivedOAuth = oAuth
                    accessToken = token
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
            )
        }.after {
        }.run {
            startAuth()
            continueAuth()
            step("Получен auth code") {
                flakySafely {
                    receivedAuthCode shouldBe AUTH_CODE
                    receivedAuthCodeSuccess = false
                }
            }
            step("Получен OAuth") {
                flakySafely {
                    receivedOAuth shouldBe oAuth
                }
            }
            step("Получен токен") {
                flakySafely {
                    accessToken?.token shouldBe MockApi.ACCESS_TOKEN
                    accessToken?.userID shouldBe MockApi.USER_ID
                    accessToken?.userData shouldBe MockApi.mockReturnedUser()
                }
            }
        }
    }

    public open fun tokenIsReceivedAfterFailedLogout(): Unit = runIfShouldNotSkip {
        var accessToken: AccessToken? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        before {
            vkidBuilder()
                .mockApiSuccess()
                .mockLogoutError()
                .user(MockApi.mockApiUser())
                .overrideDeviceId(DEVICE_ID)
                .build()
            setContent(
                onAuth = { oAuth, token ->
                    receivedOAuth = oAuth
                    accessToken = token
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
            )
        }.after {
        }.run {
            startAuth()
            continueAuth()
            step("Получен auth code") {
                flakySafely {
                    receivedAuthCode shouldBe AUTH_CODE
                    receivedAuthCodeSuccess = false
                }
            }
            step("Получен OAuth") {
                flakySafely {
                    receivedOAuth shouldBe oAuth
                }
            }
            step("Получен токен") {
                flakySafely {
                    accessToken?.token shouldBe MockApi.ACCESS_TOKEN
                    accessToken?.userID shouldBe MockApi.USER_ID
                    accessToken?.userData shouldBe MockApi.mockReturnedUser()
                }
            }
        }
    }

    public open fun authCodeIsReceived(): Unit = runIfShouldNotSkip {
        var accessToken: AccessToken? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        before {
            vkidBuilder()
                .mockApiSuccess()
                .user(MockApi.mockApiUser())
                .overrideDeviceId(DEVICE_ID)
                .build()
            setContent(
                onAuth = { oAuth, token ->
                    receivedOAuth = oAuth
                    accessToken = token
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
                authParams = VKIDAuthUiParams {
                    codeChallenge = UUID.randomUUID().toString()
                },
            )
        }.after {
        }.run {
            startAuth()
            continueAuth()
            step("Получен auth code") {
                flakySafely {
                    receivedAuthCode shouldBe AUTH_CODE
                    receivedAuthCodeSuccess = true
                }
            }
            step("Не получен OAuth") {
                flakySafely {
                    receivedOAuth.shouldBeNull()
                }
            }
            step("Не получен токен") {
                flakySafely {
                    accessToken.shouldBeNull()
                }
            }
        }
    }

    public open fun failedRedirectActivityIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        before {
            vkidBuilder()
                .notifyFailedRedirect()
                .overrideDeviceId(DEVICE_ID)
                .build()
            setContent(
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
            )
        }.after {
        }.run {
            startAuth()
            step("Auth code не получен") {
                receivedAuthCode.shouldBeNull()
                receivedAuthCodeSuccess.shouldBeNull()
            }
            step("Получена ошибка") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.FailedRedirectActivity>()
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    public open fun noBrowserAvailableIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        before {
            vkidBuilder()
                .notifyNoBrowserAvailable()
                .overrideDeviceId(DEVICE_ID)
                .build()
            setContent(
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
            )
        }.after {
        }.run {
            startAuth()
            step("Auth code не получен") {
                receivedAuthCode.shouldBeNull()
                receivedAuthCodeSuccess.shouldBeNull()
            }
            step("Получена ошибка") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.NoBrowserAvailable>()
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    public open fun failedApiCallIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        before {
            vkidBuilder()
                .mockApiError()
                .overrideDeviceId(DEVICE_ID)
                .build()
            setContent(
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
            )
        }.after {
        }.run {
            startAuth()
            continueAuth()
            step("Получен auth code") {
                flakySafely {
                    receivedAuthCode shouldBe AUTH_CODE
                    receivedAuthCodeSuccess = false
                }
            }
            step("Получена ошибка") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.FailedApiCall>()
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    public open fun failedUserCallIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        before {
            vkidBuilder()
                .mockGetTokenSuccess()
                .mockUserInfoError()
                .overrideDeviceId(DEVICE_ID)
                .build()
            setContent(
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
            )
        }.after {
        }.run {
            startAuth()
            continueAuth()
            step("Получен auth code") {
                flakySafely {
                    receivedAuthCode shouldBe AUTH_CODE
                    receivedAuthCodeSuccess = false
                }
            }
            step("Получена ошибка") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.FailedApiCall>()
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    public open fun cancellationIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        before {
            vkidBuilder()
                .mockApiSuccess()
                .overrideDeviceId(DEVICE_ID)
                .build()
            setContent(
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
            )
        }.after {
        }.run {
            startAuth()
            step("Нажатие кнопки 'назад'") {
                device.uiDevice.pressBack()
            }
            step("Auth code не получен") {
                receivedAuthCode.shouldBeNull()
                receivedAuthCodeSuccess.shouldBeNull()
            }
            step("Получена ошибка") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.Canceled>()
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    public open fun failedOAuthIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        before {
            vkidBuilder()
                .mockApiSuccess()
                .overrideOAuthToNull()
                .overrideDeviceId(DEVICE_ID)
                .build()
            setContent(
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
            )
        }.after {
        }.run {
            startAuth()
            continueAuth()
            step("Auth code не получен") {
                receivedAuthCode.shouldBeNull()
                receivedAuthCodeSuccess.shouldBeNull()
            }
            step("Получена ошибка") {
                flakySafely {
                    receivedFail.shouldBeInstanceOf<VKIDAuthFail.FailedOAuth>()
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    public open fun invalidDeviceIdIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        before {
            vkidBuilder()
                .mockApiSuccess()
                .overrideDeviceId(null)
                .build()
            setContent(
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
            )
        }.after {
        }.run {
            startAuth()
            continueAuth()
            step("Auth code не получен") {
                receivedAuthCode.shouldBeNull()
                receivedAuthCodeSuccess.shouldBeNull()
            }
            step("Получена ошибка") {
                flakySafely {
                    receivedFail shouldBe VKIDAuthFail.FailedRedirectActivity("No device id", null)
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    public open fun invalidStateIsReceived(): Unit = runIfShouldNotSkip {
        var receivedFail: VKIDAuthFail? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        before {
            vkidBuilder()
                .mockApiSuccess()
                .overrideState("wrong state")
                .overrideDeviceId(DEVICE_ID)
                .build()
            setContent(
                onFail = { oAuth, fail ->
                    receivedFail = fail
                    receivedOAuth = oAuth
                },
                onAuthCode = { authCode, isSuccess ->
                    receivedAuthCode = authCode
                    receivedAuthCodeSuccess = isSuccess
                },
            )
        }.after {
        }.run {
            startAuth()
            continueAuth()
            step("Получена ошибка") {
                step("Auth code не получен") {
                    receivedAuthCode.shouldBeNull()
                    receivedAuthCodeSuccess.shouldBeNull()
                }
                flakySafely {
                    receivedFail shouldBe VKIDAuthFail.FailedOAuthState("Invalid state")
                    receivedOAuth shouldBe oAuth
                }
            }
        }
    }

    private fun runIfShouldNotSkip(
        test: () -> Unit,
    ) {
        if (!skipTest) {
            test()
        }
    }

    protected abstract fun setContent(
        onAuth: (OAuth?, AccessToken) -> Unit = { _, _ -> },
        onAuthCode: (AuthCodeData, Boolean) -> Unit = { _, _ -> },
        onFail: (OAuth?, VKIDAuthFail) -> Unit = { _, _ -> },
        authParams: VKIDAuthUiParams = VKIDAuthUiParams {},
    )

    private fun TestContext<Unit>.continueAuth() = scenario(ContinueAuthScenario(composeTestRule))

    protected abstract fun TestContext<Unit>.startAuth()

    protected open fun vkidBuilder(): InternalVKIDTestBuilder = InternalVKIDTestBuilder(composeTestRule.activity)
}
