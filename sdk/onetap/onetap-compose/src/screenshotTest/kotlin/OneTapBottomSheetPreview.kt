@file:Suppress("DEPRECATION")
@file:OptIn(InternalVKIDApi::class)

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.vk.id.VKID
import com.vk.id.auth.VKIDAuthUiParams
import com.vk.id.common.InternalVKIDApi
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.onetap.sheet.OneTapScenario
import com.vk.id.onetap.compose.onetap.sheet.content.SheetContentAuthFailed
import com.vk.id.onetap.compose.onetap.sheet.content.SheetContentAuthSuccess
import com.vk.id.onetap.compose.onetap.sheet.content.SheetContentMain
import com.vk.id.onetap.compose.onetap.sheet.style.OneTapBottomSheetStyle

@Preview
@Composable
private fun OneTapBottomSheetFailedDartPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentAuthFailed(
        OneTapBottomSheetStyle.Dark(),
        dismissSheet = {},
        repeatClicked = {}
    )
}

@Preview
@Composable
private fun OneTapBottomSheetFailedLightPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentAuthFailed(
        OneTapBottomSheetStyle.Light(),
        dismissSheet = {},
        repeatClicked = {}
    )
}

@Preview
@Composable
private fun OneTapBottomSheetFailedTransparentDarkPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentAuthFailed(
        OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        repeatClicked = {}
    )
}

@Preview
@Composable
private fun OneTapBottomSheetFailedTransparentLightPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentAuthFailed(
        OneTapBottomSheetStyle.TransparentLight(),
        dismissSheet = {},
        repeatClicked = {}
    )
}

@Preview
@Composable
private fun OneTapBottomSheetSuccessPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentAuthSuccess(
        OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
    )
}

@Preview
@Composable
private fun OneTapBottomSheetTransparentDarkPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "Another service",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetDarkPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.Dark(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetTransparentLightPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.TransparentLight(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetLightPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.Light(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetOauthMailPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = setOf(OneTapOAuth.MAIL),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.Light(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetOauthOKPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = setOf(OneTapOAuth.OK),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.Light(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetOauthMailAndOKPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = setOf(OneTapOAuth.MAIL, OneTapOAuth.OK),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.Light(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetOauthOKAndMailPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = setOf(OneTapOAuth.OK, OneTapOAuth.MAIL),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.Light(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetsignInAnotherAccountButtonEnabledPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.Light(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = false,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetFastAuthEnabledPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterService,
        style = OneTapBottomSheetStyle.Light(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetScenarioOrderPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.Order,
        style = OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetScenarioApplicationPreview() {
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.Application,
        style = OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetScenarioEnterToAccountPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.EnterToAccount,
        style = OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetScenarioOrderInServicePreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "<Название сервиса>",
        scenario = OneTapScenario.OrderInService,
        style = OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}

@Preview
@Composable
private fun OneTapBottomSheetScenarioRegistrationForTheEventPreview() {
    VKID.Companion.initForScreenshotTests(LocalContext.current)
    SheetContentMain(
        onAuth = { _, _ -> },
        onAuthCode = { _, _ -> },
        onFail = { _, _ -> },
        oAuths = emptySet(),
        serviceName = "Кто Я такой, чтобы называть сервис",
        scenario = OneTapScenario.RegistrationForTheEvent,
        style = OneTapBottomSheetStyle.TransparentDark(),
        dismissSheet = {},
        onAuthStatusChange = {},
        authParams = VKIDAuthUiParams {},
        rememberCoroutineScope(),
        fastAuthEnabled = true,
        signInAnotherAccountButtonEnabled = true,
    )
}
