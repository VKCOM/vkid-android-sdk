@file:OptIn(InternalVKIDApi::class)

package com.vk.id.groupsubscription.base

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.vk.id.VKID
import com.vk.id.common.InternalVKIDApi
import com.vk.id.common.activity.AutoTestActivityRule
import com.vk.id.common.activity.MockProviderActivityStarter
import com.vk.id.common.allure.Owners
import com.vk.id.common.allure.Platform
import com.vk.id.common.allure.Priority
import com.vk.id.common.allure.Product
import com.vk.id.common.allure.Project
import com.vk.id.common.basetest.BaseUiTest
import com.vk.id.common.mockapi.getGroupSuccess
import com.vk.id.common.mockapi.getMembersSuccess
import com.vk.id.common.mockprovider.pm.MockPmOnlyBrowser
import com.vk.id.group.subscription.common.fail.VKIDGroupSubscriptionFail
import com.vk.id.groupsubscription.screen.GroupSubscriptionErrorScreen
import com.vk.id.groupsubscription.screen.GroupSubscriptionLoadedScreen
import com.vk.id.groupsubscription.screen.GroupSubscriptionSnackbarScreen
import com.vk.id.network.groupsubscription.exception.InternalVKIDAlreadyGroupMemberException
import com.vk.id.test.InternalVKIDTestBuilder
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.types.shouldBeInstanceOf
import io.qameta.allure.kotlin.Owner
import org.junit.Rule
import java.io.IOException

@Platform(Platform.ANDROID_AUTO)
@Product(Product.VKID_SDK)
@Project(Project.VKID_SDK)
@Owner(Owners.DANIIL_KLIMCHUK)
@Priority(Priority.CRITICAL)
@Suppress("LongMethod")
public abstract class BaseGroupSubscriptionTest : BaseUiTest() {

    @get:Rule
    public val composeTestRule: AutoTestActivityRule = createAndroidComposeRule()

    public open fun isServiceAccountFail() {
        var isSuccess = false
        var fail: VKIDGroupSubscriptionFail? = null
        before {
            vkidBuilder()
                .isServiceAccountResponse(Result.failure(IOException("Mock api error")))
                .getGroupSuccess()
                .getMembersSuccess()
                .build()
            VKID.instance.mockAuthorized()
            setContent(
                onSuccess = { isSuccess = true },
                onFail = { fail = it },
            )
        }.after {
        }.run {
            step("Шторка не видна") {
                onLoadedScreen {
                    sheet.assertIsNotDisplayed()
                }
            }
            step("Не приходит колбек об успехе") {
                flakySafely {
                    isSuccess.shouldBeFalse()
                }
            }
            step("Приходит ошибка") {
                flakySafely {
                    fail.shouldBeInstanceOf<VKIDGroupSubscriptionFail.Other>()
                }
            }
        }
    }

    public open fun isServiceAccount() {
        var isSuccess = false
        var fail: VKIDGroupSubscriptionFail? = null
        before {
            vkidBuilder()
                .isServiceAccountResponse(Result.success(true))
                .getGroupSuccess()
                .getMembersSuccess()
                .build()
            VKID.instance.mockAuthorized()
            setContent(
                onSuccess = { isSuccess = true },
                onFail = { fail = it },
            )
        }.after {
        }.run {
            step("Шторка не видна") {
                onLoadedScreen {
                    sheet.assertIsNotDisplayed()
                }
            }
            step("Не приходит колбек об успехе") {
                flakySafely {
                    isSuccess.shouldBeFalse()
                }
            }
            step("Приходит ошибка, что это сервисный аккаунт") {
                flakySafely {
                    fail.shouldBeInstanceOf<VKIDGroupSubscriptionFail.ServiceAccount>()
                }
            }
        }
    }

    public open fun alreadyAGroupMember() {
        var isSuccess = false
        var fail: VKIDGroupSubscriptionFail? = null
        before {
            vkidBuilder()
                .isServiceAccountResponse(Result.success(false))
                .getGroupResponse(Result.failure(InternalVKIDAlreadyGroupMemberException()))
                .getMembersSuccess()
                .build()
            VKID.instance.mockAuthorized()
            setContent(
                onSuccess = { isSuccess = true },
                onFail = { fail = it },
            )
        }.after {
        }.run {
            step("Шторка не видна") {
                onLoadedScreen {
                    sheet.assertIsNotDisplayed()
                }
            }
            step("Не приходит колбек об успехе") {
                flakySafely {
                    isSuccess.shouldBeFalse()
                }
            }
            step("Приходит ошибка, что это пользователь уже в группе") {
                flakySafely {
                    fail.shouldBeInstanceOf<VKIDGroupSubscriptionFail.AlreadyGroupMember>()
                }
            }
        }
    }

    public open fun getGroupError() {
        var isSuccess = false
        var fail: VKIDGroupSubscriptionFail? = null
        before {
            vkidBuilder()
                .isServiceAccountResponse(Result.success(false))
                .getGroupResponse(Result.failure(IOException("Mock api error")))
                .getMembersSuccess()
                .build()
            VKID.instance.mockAuthorized()
            setContent(
                onSuccess = { isSuccess = true },
                onFail = { fail = it },
            )
        }.after {
        }.run {
            step("Шторка не видна") {
                onLoadedScreen {
                    sheet.assertIsNotDisplayed()
                }
            }
            step("Не приходит колбек об успехе") {
                flakySafely {
                    isSuccess.shouldBeFalse()
                }
            }
            step("Приходит ошибка") {
                flakySafely {
                    fail.shouldBeInstanceOf<VKIDGroupSubscriptionFail.Other>()
                }
            }
        }
    }

    public open fun getMembersError() {
        var isSuccess = false
        var fail: VKIDGroupSubscriptionFail? = null
        before {
            vkidBuilder()
                .isServiceAccountResponse(Result.success(false))
                .getGroupSuccess()
                .getMembersResponses(Result.failure(IOException("Mock api error")), Result.failure(IOException("Mock api error")))
                .build()
            VKID.instance.mockAuthorized()
            setContent(
                onSuccess = { isSuccess = true },
                onFail = { fail = it },
            )
        }.after {
        }.run {
            step("Шторка не видна") {
                onLoadedScreen {
                    sheet.assertIsNotDisplayed()
                }
            }
            step("Не приходит колбек об успехе") {
                flakySafely {
                    isSuccess.shouldBeFalse()
                }
            }
            step("Приходит ошибка") {
                flakySafely {
                    fail.shouldBeInstanceOf<VKIDGroupSubscriptionFail.Other>()
                }
            }
        }
    }

    public open fun groupSubscriptionSuccess() {
        var isSuccess = false
        var fail: VKIDGroupSubscriptionFail? = null
        before {
            vkidBuilder()
                .isServiceAccountResponse(Result.success(false))
                .getGroupSuccess()
                .getMembersSuccess()
                .subscribeToGroupResponse(Result.success(Unit))
                .build()
            VKID.instance.mockAuthorized()
            setContent(
                onSuccess = { isSuccess = true },
                onFail = { fail = it },
            )
        }.after {
        }.run {
            step("Шторка видна") {
                onLoadedScreen {
                    sheet.assertIsDisplayed()
                }
            }
            step("Клик на подписку на сообщество") {
                onLoadedScreen {
                    subscribeButton {
                        performClick()
                    }
                }
            }
            step("Показан снекбар") {
                onSnackbarScreen {
                    snackbar.assertIsDisplayed()
                }
            }
            step("Приходит колбек об успехе") {
                flakySafely {
                    isSuccess.shouldBeTrue()
                }
            }
            step("Не приходит ошибка") {
                flakySafely {
                    fail.shouldBeNull()
                }
            }
            step("Шторка скрыта") {
                onLoadedScreen {
                    sheet.assertIsNotDisplayed()
                }
            }
        }
    }

    public open fun groupSubscriptionClose() {
        var isSuccess = false
        var fail: VKIDGroupSubscriptionFail? = null
        before {
            vkidBuilder()
                .isServiceAccountResponse(Result.success(false))
                .getGroupSuccess()
                .getMembersSuccess()
                .build()
            VKID.instance.mockAuthorized()
            setContent(
                onSuccess = { isSuccess = true },
                onFail = { fail = it },
            )
        }.after {
        }.run {
            step("Шторка видна") {
                onLoadedScreen {
                    sheet.assertIsDisplayed()
                }
            }
            step("Клик на закрытие подписки на сообщество") {
                onLoadedScreen {
                    closeButton {
                        performClick()
                    }
                }
            }
            composeTestRule.waitForIdle()
            step("Не приходит колбек об успехе") {
                flakySafely {
                    isSuccess.shouldBeFalse()
                }
            }
            step("Не приходит ошибок") {
                flakySafely {
                    fail.shouldBeNull()
                }
            }
            step("Шторка скрыта") {
                onLoadedScreen {
                    sheet.assertIsNotDisplayed()
                }
            }
        }
    }

    public open fun groupSubscriptionLater() {
        var isSuccess = false
        var fail: VKIDGroupSubscriptionFail? = null
        before {
            vkidBuilder()
                .isServiceAccountResponse(Result.success(false))
                .getGroupSuccess()
                .getMembersSuccess()
                .build()
            VKID.instance.mockAuthorized()
            setContent(
                onSuccess = { isSuccess = true },
                onFail = { fail = it },
            )
        }.after {
        }.run {
            step("Шторка видна") {
                onLoadedScreen {
                    sheet.assertIsDisplayed()
                }
            }
            step("Клик на в другой раз") {
                onLoadedScreen {
                    laterButton {
                        performClick()
                    }
                }
            }
            composeTestRule.waitForIdle()
            step("Не приходит колбек об успехе") {
                flakySafely {
                    isSuccess.shouldBeFalse()
                }
            }
            step("Не приходит ошибок") {
                flakySafely {
                    fail.shouldBeNull()
                }
            }
            step("Шторка скрыта") {
                onLoadedScreen {
                    sheet.assertIsNotDisplayed()
                }
            }
        }
    }

    public open fun groupSubscriptionFailCancel() {
        var isSuccess = false
        var fail: VKIDGroupSubscriptionFail? = null
        before {
            vkidBuilder()
                .isServiceAccountResponse(Result.success(false))
                .getGroupSuccess()
                .getMembersSuccess()
                .subscribeToGroupResponse(Result.failure(IOException("Fake api error")))
                .build()
            VKID.instance.mockAuthorized()
            setContent(
                onSuccess = { isSuccess = true },
                onFail = { fail = it },
            )
        }.after {
        }.run {
            step("Клик на подписку на сообщество, который завершается с ошибкой") {
                onLoadedScreen {
                    subscribeButton {
                        performClick()
                    }
                }
            }
            step("Клик на \"Отмена\"") {
                onErrorScreen {
                    cancelButton {
                        performClick()
                    }
                }
            }
            composeTestRule.waitForIdle()
            step("Не приходит колбек об успехе") {
                flakySafely {
                    isSuccess.shouldBeFalse()
                }
            }
            step("Не приходит ошибок") {
                flakySafely {
                    fail.shouldBeNull()
                }
            }
            step("Шторка скрыта") {
                onLoadedScreen {
                    sheet.assertIsNotDisplayed()
                }
            }
        }
    }

    public open fun groupSubscriptionRetrySuccess() {
        var isSuccess = false
        var fail: VKIDGroupSubscriptionFail? = null
        before {
            vkidBuilder()
                .isServiceAccountResponse(Result.success(false))
                .getGroupSuccess()
                .getMembersSuccess()
                .subscribeToGroupResponses(
                    listOf(
                        Result.failure(IOException("Fake api error")),
                        Result.success(Unit)
                    )
                )
                .build()
            VKID.instance.mockAuthorized()
            setContent(
                onSuccess = { isSuccess = true },
                onFail = { fail = it },
            )
        }.after {
        }.run {
            step("Клик на подписку на сообщество, который завершается с ошибкой") {
                onLoadedScreen {
                    subscribeButton {
                        performClick()
                    }
                }
            }
            step("Клик на повтор подписки") {
                onErrorScreen {
                    retryButton {
                        performClick()
                    }
                }
            }
            step("Показан снекбар") {
                onSnackbarScreen {
                    snackbar.assertIsDisplayed()
                }
            }
            step("Приходит колбек об успехе") {
                flakySafely {
                    isSuccess.shouldBeTrue()
                }
            }
            step("Не приходит ошибок") {
                flakySafely {
                    fail.shouldBeNull()
                }
            }
            step("Шторка скрыта") {
                onLoadedScreen {
                    sheet.assertIsNotDisplayed()
                }
            }
        }
    }

    public open fun groupSubscriptionRetryFail() {
        var isSuccess = false
        var fail: VKIDGroupSubscriptionFail? = null
        before {
            vkidBuilder()
                .isServiceAccountResponse(Result.success(false))
                .getGroupSuccess()
                .getMembersSuccess()
                .subscribeToGroupResponses(
                    listOf(
                        Result.failure(IOException("Fake api error")),
                        Result.failure(IOException("Fake api error")),
                        Result.success(Unit)
                    )
                )
                .build()
            VKID.instance.mockAuthorized()
            setContent(
                onSuccess = { isSuccess = true },
                onFail = { fail = it },
            )
        }.after {
        }.run {
            step("Клик на подписку на сообщество, который завершается с ошибкой") {
                onLoadedScreen {
                    subscribeButton {
                        performClick()
                    }
                }
            }
            step("Клик на повтор подписки") {
                onErrorScreen {
                    retryButton {
                        performClick()
                    }
                }
            }
            step("Eще один клик на повтор подписки") {
                onErrorScreen {
                    retryButton {
                        performClick()
                    }
                }
            }
            step("Показан снекбар") {
                onSnackbarScreen {
                    snackbar.assertIsDisplayed()
                }
            }
            step("Приходит колбек об успехе") {
                flakySafely {
                    isSuccess.shouldBeTrue()
                }
            }
            step("Не приходит ошибок") {
                flakySafely {
                    fail.shouldBeNull()
                }
            }
            step("Шторка скрыта") {
                onLoadedScreen {
                    sheet.assertIsNotDisplayed()
                }
            }
        }
    }

    private fun onLoadedScreen(action: GroupSubscriptionLoadedScreen.() -> Unit) {
        ComposeScreen.onComposeScreen<GroupSubscriptionLoadedScreen>(composeTestRule, action)
    }

    private fun onErrorScreen(action: GroupSubscriptionErrorScreen.() -> Unit) {
        ComposeScreen.onComposeScreen<GroupSubscriptionErrorScreen>(composeTestRule, action)
    }

    private fun onSnackbarScreen(action: GroupSubscriptionSnackbarScreen.() -> Unit) {
        ComposeScreen.onComposeScreen<GroupSubscriptionSnackbarScreen>(composeTestRule, action)
    }

    protected abstract fun setContent(
        onSuccess: () -> Unit,
        onFail: (VKIDGroupSubscriptionFail) -> Unit
    )

    private fun vkidBuilder(): InternalVKIDTestBuilder = InternalVKIDTestBuilder(composeTestRule.activity)
        .overridePackageManager(MockPmOnlyBrowser())
        .overrideActivityStarter(MockProviderActivityStarter(composeTestRule.activity))
}
