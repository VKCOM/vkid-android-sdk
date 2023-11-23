package com.vk.id.sample

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import com.vk.id.sample.app.MainActivity
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun launchActivity_checkButton() {
        val loginTitleString = composeTestRule.activity.getString(R.string.vkid_log_in_with_vkid)

        composeTestRule.onAllNodesWithText(loginTitleString)[0].assertIsDisplayed()
    }
}