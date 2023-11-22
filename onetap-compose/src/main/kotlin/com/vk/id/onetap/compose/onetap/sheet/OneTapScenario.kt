package com.vk.id.onetap.compose.onetap.sheet

import android.content.res.Resources
import com.vk.id.VKIDUser
import com.vk.id.onetap.compose.R
import com.vk.id.onetap.compose.button.VKIDButtonTextProvider

public enum class OneTapScenario {
    EnterService,
    RegistrationForTheEvent,
    Application,
    OrderInService,
    Order,
    EnterToAccount
}

internal fun OneTapScenario.scenarioTitle(resources: Resources, serviceName: String): String = when (this) {
    OneTapScenario.EnterService -> resources.getString(R.string.vkid_schenario_enter_service_title)
    OneTapScenario.RegistrationForTheEvent -> resources.getString(
        R.string.vkid_schenario_registration_for_the_event_title
    )
    OneTapScenario.Application -> resources.getString(R.string.vkid_schenario_application_title)
    OneTapScenario.OrderInService -> resources.getString(R.string.vkid_schenario_order_in_service_title, serviceName)
    OneTapScenario.Order -> resources.getString(R.string.vkid_schenario_order_title)
    OneTapScenario.EnterToAccount -> resources.getString(R.string.vkid_schenario_enter_to_account_title, serviceName)
}

internal fun OneTapScenario.vkidButtonTextProvider(resources: Resources): VKIDButtonTextProvider {
    return object : VKIDButtonTextProvider {
        override fun userFoundText(user: VKIDUser): String {
            val userName = user.firstName
            return when (this@vkidButtonTextProvider) {
                OneTapScenario.EnterService -> resources.getString(
                    R.string.vkid_schenario_enter_service_vkid_button_text_user_found,
                    userName
                )

                OneTapScenario.RegistrationForTheEvent -> resources.getString(
                    R.string.vkid_schenario_registration_for_the_event_vkid_button_text_user_found,
                    userName
                )

                OneTapScenario.Application -> resources.getString(
                    R.string.vkid_schenario_application_vkid_button_text_user_found,
                    userName
                )

                OneTapScenario.OrderInService -> resources.getString(
                    R.string.vkid_schenario_order_in_service_vkid_button_text_user_found,
                    userName
                )

                OneTapScenario.Order -> resources.getString(
                    R.string.vkid_schenario_order_vkid_button_text_user_found,
                    userName
                )

                OneTapScenario.EnterToAccount -> resources.getString(
                    R.string.vkid_schenario_enter_to_account_vkid_button_text_user_found,
                    userName
                )
            }
        }

        override fun noUserText(): String =
            when (this@vkidButtonTextProvider) {
                OneTapScenario.EnterService -> resources.getString(
                    R.string.vkid_schenario_enter_service_vkid_button_text_no_user
                )
                OneTapScenario.RegistrationForTheEvent -> resources.getString(
                    R.string.vkid_schenario_registration_for_the_event_vkid_button_text_no_user
                )
                OneTapScenario.Application -> resources.getString(
                    R.string.vkid_schenario_application_vkid_button_text_no_user
                )
                OneTapScenario.OrderInService -> resources.getString(
                    R.string.vkid_schenario_order_in_service_vkid_button_text_no_user
                )
                OneTapScenario.Order -> resources.getString(R.string.vkid_schenario_order_vkid_button_text_no_user)
                OneTapScenario.EnterToAccount -> resources.getString(
                    R.string.vkid_schenario_enter_to_account_vkid_button_text_no_user
                )
            }
    }
}
