package com.vk.id.groupsubscription.xml

import android.os.Handler
import android.os.Looper
import com.vk.id.common.feature.TestFeature
import com.vk.id.group.subscription.common.fail.VKIDGroupSubscriptionFail
import com.vk.id.group.subscription.xml.GroupSubscriptionSheet
import com.vk.id.groupsubscription.base.BaseGroupSubscriptionTest
import io.qameta.allure.kotlin.AllureId
import io.qameta.allure.kotlin.Feature
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Test

@Feature(TestFeature.GROUP_SUBSCRIPTION)
@DisplayName("XML Подписка на сообщество")
public class GroupSubscriptionXmlTest : BaseGroupSubscriptionTest() {

    @Test
    @AllureId("2413533")
    @DisplayName("Получение ошибки серверных лимитов XML")
    override fun remoteLimitReached() {
        super.remoteLimitReached()
    }

    @Test
    @AllureId("2412804")
    @DisplayName("Получение ошибки клиентских лимитов XML")
    override fun limitReached() {
        super.limitReached()
    }

    @Test
    @AllureId("2386913")
    @DisplayName("Ошибка при получении статуса сервисного аккаунта XML")
    override fun isServiceAccountFail() {
        super.isServiceAccountFail()
    }

    @Test
    @AllureId("2386894")
    @DisplayName("Открытие шторки для сервисного аккаунта XML")
    override fun isServiceAccount() {
        super.isServiceAccount()
    }

    @Test
    @AllureId("2386911")
    @DisplayName("Получение ошибки что пользователь уже в группе XML")
    override fun alreadyAGroupMember() {
        super.alreadyAGroupMember()
    }

    @Test
    @AllureId("2386903")
    @DisplayName("Ошибка при получении данных о группе XML")
    override fun getGroupError() {
        super.getGroupError()
    }

    @Test
    @AllureId("2386906")
    @DisplayName("Ошибка при получении данных о членах группы XML")
    override fun getMembersError() {
        super.getMembersError()
    }

    @Test
    @AllureId("2386914")
    @DisplayName("Успешная подписка на сообщество XML")
    override fun groupSubscriptionSuccess() {
        super.groupSubscriptionSuccess()
    }

    @Test
    @AllureId("2386910")
    @DisplayName("Закрытие подписки на сообщество XML")
    override fun groupSubscriptionClose() {
        super.groupSubscriptionClose()
    }

    @Test
    @AllureId("2386896")
    @DisplayName("Нажатие на в другой раз XML")
    override fun groupSubscriptionLater() {
        super.groupSubscriptionLater()
    }

    @Test
    @AllureId("2386895")
    @DisplayName("Отмена после ошибки подписки на сообщество XML")
    override fun groupSubscriptionFailCancel() {
        super.groupSubscriptionFailCancel()
    }

    @Test
    @AllureId("2386900")
    @DisplayName("Успешный повтор пописки на сообщество XML")
    override fun groupSubscriptionRetrySuccess() {
        super.groupSubscriptionRetrySuccess()
    }

    @Test
    @AllureId("2386907")
    @DisplayName("Ошибка при повторе пописки на сообщество XML")
    override fun groupSubscriptionRetryFail() {
        super.groupSubscriptionRetryFail()
    }

    override fun setContent(
        onSuccess: () -> Unit,
        onFail: (VKIDGroupSubscriptionFail) -> Unit
    ) {
        val view = GroupSubscriptionSheet(composeTestRule.activity).apply {
            setCallbacks(
                onSuccess = onSuccess,
                onFail = onFail
            )
            this.groupId = "1"
        }
        composeTestRule.activity.setContent(view)
        Handler(Looper.getMainLooper()).post { view.show() }
    }
}
