package com.vk.id.bottomsheet.compose

import com.vk.id.bottomsheet.base.BottomSheetFlowTest
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Suppress("EmptyFunctionBlock")
public class BottomSheetFlowComposeTest : BottomSheetFlowTest() {

    @Test
    @AllureId("2315339")
    @DisplayName("Успешная авторизация после ретрая в Compose BottomSheet")
    override fun authSuccessAfterRetry() {
    }

    @Test
    @AllureId("2315344")
    @DisplayName("Успешная смена аккаунта после ретрая в Compose BottomSheet")
    override fun changeAccountSuccessAfterRetry() {
    }

    @Test
    @AllureId("2315335")
    @DisplayName("Ошибка авторизации после ретрая в Compose BottomSheet")
    override fun authFailAfterRetry() {
    }

    @Test
    @AllureId("2315342")
    @DisplayName("Ошибка смены аккаунта после ретрая в Compose BottomSheet")
    override fun changeAccountFailAfterRetry() {
    }

    @Test
    @AllureId("2315337")
    @DisplayName("Шторка не скрывается после авторизации в Compose BottomSheet")
    override fun sheetNotHiddenAfterAuth() {
    }

    @Test
    @AllureId("2315343")
    @DisplayName("Автоскрытие шторки в Compose BottomSheet")
    override fun sheetAuthHide() {
    }
}
