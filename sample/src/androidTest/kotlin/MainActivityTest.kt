import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun launchActivity_checkButton() {
        composeTestRule.setContent {

        }

        composeTestRule.onNodeWithText("Log in with VK ID").performClick()
    }
}