package com.vk.id.onetap.xml

import android.os.Handler
import android.os.Looper
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.AuthCodeData
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.allure.Feature
import com.vk.id.common.feature.TestFeature
import com.vk.id.multibranding.base.MultibrandingTest
import com.vk.id.onetap.common.OneTapOAuth
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.MULTIBRANDING)
@DisplayName("Мультибрендинг в XML BottomSheet")
public class BottomSheetMultibrandingXmlTest(
    private val oAuth: OAuth,
) : MultibrandingTest(oAuth, skipTest = oAuth == OAuth.VK) {

    @Test
    @AllureId("2290788")
    @DisplayName("Успешное получение токена в XML BottomSheet Мультибрендинге")
    override fun tokenIsReceived() {
        super.tokenIsReceived()
    }

    @Test
    @AllureId("2303006")
    @DisplayName("Успешное получение токена после логаута в Compose OneTap")
    override fun tokenIsReceivedAfterFailedLogout() {
        super.tokenIsReceivedAfterFailedLogout()
    }

    @Test
    @AllureId("2290730")
    @DisplayName("Получение ошибочного редиректа в Activity в XML BottomSheet Мультибрендинге")
    override fun failedRedirectActivityIsReceived() {
        super.failedRedirectActivityIsReceived()
    }

    @Test
    @AllureId("2290808")
    @DisplayName("Получение ошибки отсутсвия браузера в XML BottomSheet Мультибрендинге")
    override fun noBrowserAvailableIsReceived() {
        super.noBrowserAvailableIsReceived()
    }

    @Test
    @AllureId("2290819")
    @DisplayName("Получение ошибки апи в XML BottomSheet Мультибрендинге")
    override fun failedApiCallIsReceived() {
        super.failedApiCallIsReceived()
    }

    @Test
    @AllureId("2290813")
    @DisplayName("Получение ошибки отмены авторизации в XML BottomSheet Мультибрендинге")
    override fun cancellationIsReceived() {
        super.cancellationIsReceived()
    }

    @Test
    @AllureId("2290786")
    @DisplayName("Получение ошибки отсутствия данных oauth в XML BottomSheet Мультибрендинге")
    override fun failedOAuthIsReceived() {
        super.failedOAuthIsReceived()
    }

    @Test
    @AllureId("2290799")
    @DisplayName("Получение ошибки отсутствия deviceId в XML BottomSheet Мультибрендинге")
    override fun invalidDeviceIdIsReceived() {
        super.invalidDeviceIdIsReceived()
    }

    @Test
    @AllureId("2290795")
    @DisplayName("Получение ошибки неверного state в XML BottomShee Мультибрендингеt")
    override fun invalidStateIsReceived() {
        super.invalidStateIsReceived()
    }

    @Test
    @AllureId("2302979")
    @DisplayName("Успешное получение кода при схеме с бекендом в XML OneTap Мультибрендинге")
    override fun authCodeIsReceived() {
        super.authCodeIsReceived()
    }

    @Test
    @AllureId("2303002")
    @DisplayName("Получение ошибки загрузки пользовательских данных в Compose OneTap")
    override fun failedUserCallIsReceived() {
        super.failedUserCallIsReceived()
    }

    override fun setContent(
        vkid: VKID,
        onAuth: (OAuth?, AccessToken) -> Unit,
        onAuthCode: (AuthCodeData, Boolean) -> Unit,
        onFail: (OAuth?, VKIDAuthFail) -> Unit,
        authParams: VKIDAuthUiParams,
    ) {
        val view = OneTapBottomSheet(composeTestRule.activity).apply {
            setCallbacks(
                onAuth = { oAuth, accessToken -> onAuth(oAuth?.toOAuth(), accessToken) },
                onAuthCode = onAuthCode,
                onFail = { oAuth, fail -> onFail(oAuth?.toOAuth(), fail) },
            )
            setVKID(vkid)
            this.authParams = authParams
            oAuths = setOfNotNull(OneTapOAuth.fromOAuth(oAuth))
        }
        composeTestRule.activity.setContent(view)
        Handler(Looper.getMainLooper()).post { view.show() }
    }
}
