package com.vk.id.bottomsheet.xml

import com.vk.id.bottomsheet.base.BottomSheetFlowTest
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Suppress("EmptyFunctionBlock")
public class BottomSheetFlowXmlTest : BottomSheetFlowTest() {

    @Test
    @AllureId("2315336")
    @DisplayName("Успешная авторизация после ретрая в XML BottomSheet")
    override fun authSuccessAfterRetry() {
    }

    @Test
    @AllureId("2315340")
    @DisplayName("Успешная смена аккаунта после ретрая в XML BottomSheet")
    override fun changeAccountSuccessAfterRetry() {
    }

    @Test
    @AllureId("2315346")
    @DisplayName("Ошибка авторизации после ретрая в XML BottomSheet")
    override fun authFailAfterRetry() {
    }

    @Test
    @AllureId("2315341")
    @DisplayName("Ошибка смены аккаунта после ретрая в XML BottomSheet")
    override fun changeAccountFailAfterRetry() {
    }

    @Test
    @AllureId("2315347")
    @DisplayName("Шторка не скрывается после авторизации в XML BottomSheet")
    override fun sheetNotHiddenAfterAuth() {
    }

    @Test
    @AllureId("2315345")
    @DisplayName("Автоскрытие шторки в XML BottomSheet")
    override fun sheetAuthHide() {
    }
}
