package com.vk.id.groupsubscription.compose

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.vk.id.common.feature.TestFeature
import com.vk.id.group.subscription.common.fail.VKIDGroupSubscriptionFail
import com.vk.id.group.subscription.compose.ui.GroupSubscriptionSheet
import com.vk.id.group.subscription.compose.ui.rememberGroupSubscriptionSheetState
import com.vk.id.groupsubscription.base.BaseGroupSubscriptionTest
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.GROUP_SUBSCRIPTION)
@DisplayName("Compose Подписка на сообщество")
public class GroupSubscriptionComposeTest : BaseGroupSubscriptionTest() {

    @Test
    @AllureId("2412805")
    @DisplayName("Получение ошибки клиентских лимитов Compose")
    override fun limitReached() {
        super.limitReached()
    }

    @Test
    @AllureId("2386902")
    @DisplayName("Ошибка при получении статуса сервисного аккаунта Compose")
    override fun isServiceAccountFail() {
        super.isServiceAccountFail()
    }

    @Test
    @AllureId("2386904")
    @DisplayName("Открытие шторки для сервисного аккаунта Compose")
    override fun isServiceAccount() {
        super.isServiceAccount()
    }

    @Test
    @AllureId("2386912")
    @DisplayName("Получение ошибки что пользователь уже в группе Compose")
    override fun alreadyAGroupMember() {
        super.alreadyAGroupMember()
    }

    @Test
    @AllureId("2386897")
    @DisplayName("Ошибка при получении данных о группе Compose")
    override fun getGroupError() {
        super.getGroupError()
    }

    @Test
    @AllureId("2386893")
    @DisplayName("Ошибка при получении данных о членах группы Compose")
    override fun getMembersError() {
        super.getMembersError()
    }

    @Test
    @AllureId("2386901")
    @DisplayName("Успешная подписка на сообщество Compose")
    override fun groupSubscriptionSuccess() {
        super.groupSubscriptionSuccess()
    }

    @Test
    @AllureId("2386905")
    @DisplayName("Закрытие подписки на сообщество Compose")
    override fun groupSubscriptionClose() {
        super.groupSubscriptionClose()
    }

    @Test
    @AllureId("2386898")
    @DisplayName("Нажатие на в другой раз Compose")
    override fun groupSubscriptionLater() {
        super.groupSubscriptionLater()
    }

    @Test
    @AllureId("2386909")
    @DisplayName("Отмена после ошибки подписки на сообщество Compose")
    override fun groupSubscriptionFailCancel() {
        super.groupSubscriptionFailCancel()
    }

    @Test
    @AllureId("2386908")
    @DisplayName("Успешный повтор пописки на сообщество Compose")
    override fun groupSubscriptionRetrySuccess() {
        super.groupSubscriptionRetrySuccess()
    }

    @Test
    @AllureId("2386899")
    @DisplayName("Ошибка при повторе пописки на сообщество Compose")
    override fun groupSubscriptionRetryFail() {
        super.groupSubscriptionRetryFail()
    }

    override fun setContent(
        onSuccess: () -> Unit,
        onFail: (VKIDGroupSubscriptionFail) -> Unit
    ) {
        composeTestRule.setContent {
            val state = rememberGroupSubscriptionSheetState()
            GroupSubscriptionSheet(
                modifier = Modifier.fillMaxSize(),
                groupId = "1",
                state = state,
                onSuccess = onSuccess,
                onFail = onFail,
            )
            Handler(Looper.getMainLooper()).post {
                state.show()
            }
        }
    }
}
