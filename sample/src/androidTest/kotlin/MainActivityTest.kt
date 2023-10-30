import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.vk.id.sample.MainActivity
import com.vk.id.sample.R
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun launchActivity_checkButton() {
        composeTestRule.setContent { MainActivity() }
        val loginTitleString = composeTestRule.activity.getString(R.string.vkid_log_in_with_vkid)

        composeTestRule.onNodeWithText(loginTitleString).assertExists()
    }
}