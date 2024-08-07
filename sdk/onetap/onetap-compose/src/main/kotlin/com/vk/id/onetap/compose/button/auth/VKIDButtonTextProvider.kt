package com.vk.id.onetap.compose.button.auth

import com.vk.id.VKIDUser
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario

internal interface VKIDButtonTextProvider {
    fun userFoundText(user: VKIDUser, scenario: OneTapTitleScenario): String
    fun userFoundShortText(user: VKIDUser, scenario: OneTapTitleScenario): String
    fun noUserText(scenario: OneTapTitleScenario): String
    fun noUserShortText(scenario: OneTapTitleScenario): String
}
