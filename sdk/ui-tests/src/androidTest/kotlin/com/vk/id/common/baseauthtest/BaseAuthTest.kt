@file:OptIn(InternalVKIDApi::class)

package com.vk.id.common.baseauthtest

import android.net.Uri
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.vk.id.AccessToken
import com.vk.id.BuildConfig
import com.vk.id.OAuth
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.activity.AutoTestActivityRule
import com.vk.id.common.activity.MockProviderActivityStarter
import com.vk.id.common.allure.Owners
import com.vk.id.common.allure.Platform
import com.vk.id.common.allure.Priority
import com.vk.id.common.allure.Product
import com.vk.id.common.allure.Project
import com.vk.id.common.basetest.BaseUiTestWithProvider
import com.vk.id.common.mockapi.MockApi
import com.vk.id.common.mockapi.mockApiError
import com.vk.id.common.mockapi.mockApiSuccess
import com.vk.id.common.mockapi.mockGetTokenSuccess
import com.vk.id.common.mockapi.mockLogoutError
import com.vk.id.common.mockapi.mockUserInfoError
import com.vk.id.common.mockprovider.ContinueAuthScenario
import com.vk.id.common.mockprovider.pm.MockPmNoProvidersNoBrowsers
import com.vk.id.common.mockprovider.pm.MockPmOnlyBrowser
import com.vk.id.test.InternalVKIDTestBuilder
import com.vk.id.util.ServiceCredentials
import com.vk.id.util.readVKIDCredentials
import com.vk.id.util.shouldHaveExactSetOfParameters
import com.vk.id.util.shouldHaveHost
import com.vk.id.util.shouldHaveParameter
import com.vk.id.util.shouldHavePath
import com.vk.id.util.shouldHaveScheme
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
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
) : BaseUiTestWithProvider() {

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

    internal lateinit var serviceCredentials: ServiceCredentials

    @Before
    public fun readCreds() {
        serviceCredentials = readVKIDCredentials(composeTestRule.activity)
    }

    @Suppress("LongMethod")
    public open fun tokenIsReceived(): Unit = runIfShouldNotSkip {
        var accessToken: AccessToken? = null
        var receivedOAuth: OAuth? = null
        var receivedAuthCode: AuthCodeData? = null
        var receivedAuthCodeSuccess: Boolean? = null
        var providerReceivedUri: Uri? = null
        before {
            vkidBuilder()
                .mockApiSuccess()
                .build()
            user(MockApi.mockApiUser())
            overrideDeviceId(DEVICE_ID)
            onProviderReceivedUri {
                providerReceivedUri = it
            }
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
            step("Провайдер получил нужные параметры в интенте") {
                flakySafely {
                    checkProviderReceivedUri(providerReceivedUri)
                    providerReceivedUri?.shouldHaveExactSetOfParameters(supportedUriParams)
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
                .build()
            user(MockApi.mockApiUser())
            overrideDeviceId(DEVICE_ID)
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
                .build()
            user(MockApi.mockApiUser())
            overrideDeviceId(DEVICE_ID)
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
            vkidBuilder().build()
            deviceIdIsNull()
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
            vkidBuilder().overridePackageManager(MockPmNoProvidersNoBrowsers()).build()
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
                .build()
            overrideDeviceId(DEVICE_ID)
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
                .build()
            overrideDeviceId(DEVICE_ID)
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
                .build()
            overrideDeviceId(DEVICE_ID)
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
                .build()
            overrideOAuthToNull()
            overrideDeviceId(DEVICE_ID)
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
                .build()
            deviceIdIsNull()
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
                .build()
            overrideState("wrong state")
            overrideDeviceId(DEVICE_ID)
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

    protected open val supportedUriParams: Set<String> =
        setOf(
            "client_id",
            "response_type",
            "redirect_uri",
            "code_challenge_method",
            "code_challenge",
            "state",
            "prompt",
            "stats_info",
            "sdk_type",
            "v",
            "lang_id",
            "scheme"
        )

    protected open fun checkProviderReceivedUri(providerReceivedUri: Uri?) {
        providerReceivedUri?.shouldHaveScheme("https")
        providerReceivedUri?.shouldHaveHost("id.vk.com")
        providerReceivedUri?.shouldHavePath("/authorize")
        providerReceivedUri?.shouldHaveParameter("client_id", serviceCredentials.clientID)
        providerReceivedUri?.shouldHaveParameter("response_type", "code")
        providerReceivedUri?.shouldHaveParameter("code_challenge_method", "s256")
        providerReceivedUri?.shouldHaveParameter("sdk_type", "vkid")
        providerReceivedUri?.shouldHaveParameter("v", BuildConfig.VKID_VERSION_NAME)
        val redirectUri = providerReceivedUri?.getQueryParameter("redirect_uri")
        redirectUri shouldStartWith serviceCredentials.redirectUri + "?oauth2_params="
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
        .overridePackageManager(MockPmOnlyBrowser())
        .overrideActivityStarter(MockProviderActivityStarter(composeTestRule.activity))
}
