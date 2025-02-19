@file:OptIn(InternalVKIDApi::class)

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.VKID
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario

@Preview
@Composable
private fun OneTapSignUp() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OneTap(
        onAuth = { _, _ -> },
        scenario = OneTapTitleScenario.SignUp,
    )
}

@Preview
@Composable
private fun OneTapGet() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OneTap(
        onAuth = { _, _ -> },
        scenario = OneTapTitleScenario.Get,
    )
}

@Preview
@Composable
private fun OneTapOpen() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OneTap(
        onAuth = { _, _ -> },
        scenario = OneTapTitleScenario.Open,
    )
}

@Preview
@Composable
private fun OneTapCalculate() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OneTap(
        onAuth = { _, _ -> },
        scenario = OneTapTitleScenario.Calculate,
    )
}

@Preview
@Composable
private fun OneTapOrder() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OneTap(
        onAuth = { _, _ -> },
        scenario = OneTapTitleScenario.Order,
    )
}

@Preview
@Composable
private fun OneTapPlaceOrder() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OneTap(
        onAuth = { _, _ -> },
        scenario = OneTapTitleScenario.PlaceOrder,
    )
}

@Preview
@Composable
private fun OneTapSendRequest() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OneTap(
        onAuth = { _, _ -> },
        scenario = OneTapTitleScenario.SendRequest,
    )
}

@Preview
@Composable
private fun OneTapParticipate() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    OneTap(
        onAuth = { _, _ -> },
        scenario = OneTapTitleScenario.Participate,
    )
}
