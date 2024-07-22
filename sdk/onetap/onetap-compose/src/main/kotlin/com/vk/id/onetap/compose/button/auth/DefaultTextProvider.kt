package com.vk.id.onetap.compose.button.auth

import android.content.res.Resources
import com.vk.id.VKIDUser
import com.vk.id.onetap.compose.R
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario.Calculate
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario.Get
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario.Open
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario.Order
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario.Participate
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario.PlaceOrder
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario.SendRequest
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario.SignIn
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario.SignUp

internal class DefaultTextProvider(
    private val resources: Resources
) : VKIDButtonTextProvider {
    override fun userFoundText(user: VKIDUser, scenario: OneTapTitleScenario) = when (scenario) {
        SignIn -> R.string.vkid_log_in_as
        SignUp -> R.string.vkid_sign_up_as
        Get -> R.string.vkid_get_as
        Open -> R.string.vkid_open_as
        Calculate -> R.string.vkid_calculate_as
        Order -> R.string.vkid_order_as
        PlaceOrder -> R.string.vkid_place_order_as
        SendRequest -> R.string.vkid_send_request_as
        Participate -> R.string.vkid_participate_as
    }.let { resources.getString(it, user.firstName) }

    override fun userFoundShortText(user: VKIDUser, scenario: OneTapTitleScenario) = when (scenario) {
        SignIn -> R.string.vkid_log_in
        SignUp -> R.string.vkid_sign_up
        Get -> R.string.vkid_get
        Open -> R.string.vkid_open
        Calculate -> R.string.vkid_calculate
        Order -> R.string.vkid_order
        PlaceOrder -> R.string.vkid_place_order
        SendRequest -> R.string.vkid_send_request
        Participate -> R.string.vkid_participate
    }.let(resources::getString)

    override fun noUserText(scenario: OneTapTitleScenario) = when (scenario) {
        SignIn -> R.string.vkid_log_in_with_vkid
        SignUp -> R.string.vkid_sign_up_with_vkid
        Get -> R.string.vkid_get_with_vkid
        Open -> R.string.vkid_open_with_vkid
        Calculate -> R.string.vkid_calculate_with_vkid
        Order -> R.string.vkid_order_with_vkid
        PlaceOrder -> R.string.vkid_place_order_with_vkid
        SendRequest -> R.string.vkid_send_request_with_vkid
        Participate -> R.string.vkid_participate_with_vkid
    }.let(resources::getString)

    override fun noUserShortText(scenario: OneTapTitleScenario) = resources.getString(R.string.vkid_log_in_with_vkid_short)
}
