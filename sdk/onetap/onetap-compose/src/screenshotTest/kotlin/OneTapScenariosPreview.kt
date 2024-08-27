import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario

@Preview
@Composable
private fun OneTapSignUp() {
    OneTap(
        onAuth = { _, _ -> },
        scenario = OneTapTitleScenario.SignUp,
    )
}

@Preview
@Composable
private fun OneTapGet() {
    OneTap(
        onAuth = { _, _ -> },
        scenario = OneTapTitleScenario.Get,
    )
}

@Preview
@Composable
private fun OneTapOpen() {
    OneTap(
        onAuth = { _, _ -> },
        scenario = OneTapTitleScenario.Open,
    )
}

@Preview
@Composable
private fun OneTapCalculate() {
    OneTap(
        onAuth = { _, _ -> },
        scenario = OneTapTitleScenario.Calculate,
    )
}

@Preview
@Composable
private fun OneTapOrder() {
    OneTap(
        onAuth = { _, _ -> },
        scenario = OneTapTitleScenario.Order,
    )
}

@Preview
@Composable
private fun OneTapPlaceOrder() {
    OneTap(
        onAuth = { _, _ -> },
        scenario = OneTapTitleScenario.PlaceOrder,
    )
}

@Preview
@Composable
private fun OneTapSendRequest() {
    OneTap(
        onAuth = { _, _ -> },
        scenario = OneTapTitleScenario.SendRequest,
    )
}

@Preview
@Composable
private fun OneTapParticipate() {
    OneTap(
        onAuth = { _, _ -> },
        scenario = OneTapTitleScenario.Participate,
    )
}
