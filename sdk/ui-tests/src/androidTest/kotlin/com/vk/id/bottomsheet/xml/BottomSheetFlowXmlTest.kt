package com.vk.id.bottomsheet.xml

import android.os.Handler
import android.os.Looper
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.bottomsheet.base.BottomSheetFlowTest
import com.vk.id.common.allure.Feature
import com.vk.id.common.feature.TestFeature
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.xml.OneTapBottomSheet
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.BOTTOM_SHEET)
@DisplayName("Авторизация с помощью Bottomsheet")
@Suppress("LongMethod")
public class BottomSheetFlowXmlTest : BottomSheetFlowTest() {

    @Test
    @AllureId("2315336")
    @DisplayName("Успешная авторизация после ретрая в XML BottomSheet")
    override fun authSuccessAfterRetry() {
        super.authSuccessAfterRetry()
    }

    @Test
    @AllureId("2315340")
    @DisplayName("Успешная смена аккаунта после ретрая в XML BottomSheet")
    override fun changeAccountSuccessAfterRetry() {
        super.changeAccountSuccessAfterRetry()
    }

    @Test
    @AllureId("2315346")
    @DisplayName("Ошибка авторизации после ретрая в XML BottomSheet")
    override fun authFailAfterRetry() {
        super.authFailAfterRetry()
    }

    @Test
    @AllureId("2315341")
    @DisplayName("Ошибка смены аккаунта после ретрая в XML BottomSheet")
    override fun changeAccountFailAfterRetry() {
        super.changeAccountFailAfterRetry()
    }

    @Test
    @AllureId("2315347")
    @DisplayName("Шторка не скрывается после авторизации в XML BottomSheet")
    override fun sheetNotHiddenAfterAuth() {
        super.sheetNotHiddenAfterAuth()
    }

    @Test
    @AllureId("2315345")
    @DisplayName("Автоскрытие шторки в XML BottomSheet")
    override fun sheetAuthHide() {
        super.sheetAuthHide()
    }

    override fun setContent(
        onAuth: (OAuth?, AccessToken) -> Unit,
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onFail: (OAuth?, VKIDAuthFail) -> Unit,
        authParams: VKIDAuthUiParams,
        autoHideOnSuccess: Boolean
    ) {
        val view = OneTapBottomSheet(composeTestRule.activity).apply {
            setCallbacks(
                onAuth = { oAuth, token -> onAuth(oAuth?.toOAuth(), token) },
                onAuthCode = onAuthCode,
                onFail = { oAuth, fail -> onFail(oAuth?.toOAuth(), fail) }
            )
            this.authParams = authParams
        }
        composeTestRule.activity.setContent(view)
        Handler(Looper.getMainLooper()).post { view.show() }
    }}
