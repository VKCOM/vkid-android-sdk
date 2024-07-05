package com.vk.id.bottomsheet.base

import com.vk.id.OAuth
import com.vk.id.common.allure.Owners
import com.vk.id.common.allure.Platform
import com.vk.id.common.allure.Priority
import com.vk.id.common.allure.Product
import com.vk.id.common.allure.Project
import com.vk.id.common.baseauthtest.BaseAuthTest
import com.vk.id.common.basetest.BaseUiTest
import io.qameta.allure.kotlin.Owner

@Platform(Platform.ANDROID_AUTO)
@Product(Product.VKID_SDK)
@Project(Project.VKID_SDK)
@Owner(Owners.MAKSIM_SPIRIDONOV)
@Priority(Priority.CRITICAL)
public abstract class BottomSheetFlowTest : BaseUiTest() {

    abstract fun authSuccessAfterRetry()

    abstract fun changeAccountSuccessAfterRetry()

    abstract fun authFailAfterRetry()

    abstract fun changeAccountFailAfterRetry()

    abstract fun sheetNotHiddenAfterAuth()

    abstract fun sheetAuthHide()
}
